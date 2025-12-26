document.addEventListener("DOMContentLoaded", () => {

    // 1️⃣ Dati del professore
    fetch("/api/professor/me",{
        method: "POST"
    })
        .then(res => res.json())
        .then(p => {
            document.getElementById("welcome").textContent =
                `Benvenuto Professore ${p.name} ${p.surname}`;
        });

    // 2️⃣ Corsi + appelli
    fetch("/api/courses")
        .then(res => res.json())
        .then(data => renderCourses(data));
});

const columns = new Map([
    ["Matricola", {
        type: "string",
        accessor: s => s.student.id
    }],
    ["Cognome", {
        type: "string",
        accessor: s => s.student.surname
    }],
    ["Nome", {
        type: "string",
        accessor: s => s.student.name
    }],
    ["Email", {
        type: "string",
        accessor: s => s.student.email
    }],
    ["Corso", {
        type: "string",
        accessor: s => s.student.degreeProgramCode
    }],
    ["Voto", {
        type: "number",
        accessor: s => Number(s.examResult.grade.label)
    }],
    ["Stato", {
        type: "string",
        accessor: s => s.examResult.status.label
    }],
    ["Session", {
        type: "number",
        accessor: s => s.examResult.examId
    }]
]);

function renderCourses(courses) {
    const container = document.getElementById("coursesContainer");
    container.innerHTML = "";

    courses.forEach(c => {

        // course box
        const courseBox = document.createElement("div");
        courseBox.className = "course-box";
        courseBox.textContent = `${c.name} (${c.code})`;
        courseBox.onclick = () => toggleSessions(c.code);

        // session list
        const sessionList = document.createElement("div");
        sessionList.className = "session-list";
        sessionList.id = "sessions-" + c.code;
        sessionList.style.display = "none";

        c.examSessions.forEach(s => {
            const item = document.createElement("div");
            item.className = "session-item";
            item.textContent = `${s.date} - ${s.type}`;
            item.onclick = () => showEnrolledStudents(item , s.id);
            sessionList.appendChild(item);
        });

        container.appendChild(courseBox);
        container.appendChild(sessionList);
    });
}
function toggleSessions(courseCode) {
    const box = document.getElementById("sessions-" + courseCode);
    box.style.display = (box.style.display === "block") ? "none" : "block";
}
function logout() {
    fetch("/api/logout",
        { method: "POST" })
        .then(
            () =>
                window.location.href = "/html/login.html"
        );
}

// BEGIN RENDERING MANAGEMENT FUNCTION
function showEnrolledStudents(examSessionElement, examSessionId) {
    // toggle: se già aperto → chiudi
    const existing = examSessionElement.querySelector(".enrolled-container");
    if (existing) {
        existing.remove();
        return;
    }

    // contenitore iscritti
    const container = document.createElement("div");
    container.className = "enrolled-container";
    container.id = "enrolled-container-"+examSessionId;
    container.textContent = "Caricamento iscritti...";
    examSessionElement.appendChild(container);

    fetch(`/api/enrolled?examSessionId=${examSessionId}`)
        .then(res => {
            if (!res.ok) {
                throw new Error("Errore server");
            }
            return res.json();
        })
        .then(students => renderEnrolledStudents(container, students))
        .catch(err => {
            container.textContent = "Errore nel caricamento iscritti";
            console.error(err);
        });
}
function renderEnrolledStudents(container, students) {
    if (students.length === 0) {
        container.textContent = "Nessuno studente iscritto";
        return;
    }

    if(typeof (Storage) !== "undefined"){
        if(sessionStorage.getItem("rows") === null){
            /*
            * example of a row:
            * {
                Matricola: student.id,
                Cognome: student.surname,
                Nome: student.name,
                Email: student.email,
                Corso: student.degreeProgramCode,
                Voto: Number(examResult.grade.label),
                Stato: examResult.status.label
                Session: examResult.examId
              }
            * */
            const key = "rows-"+container.id;
            sessionStorage.setItem(key, JSON.stringify(
                {
                    "students": students.map(student => {
                        const row = {};
                        columns.forEach((meta, columnName) => {
                            row[columnName] = meta.accessor(student);
                        });
                        return row;
                    }),
                    "sortState": {
                        column: null,
                        direction: "asc" // or "desc"
                    }
                })
            );
            console.log(JSON.parse(sessionStorage.getItem(key)));
            renderTable(container, JSON.parse(sessionStorage.getItem(key))["students"]);
        }
    }
    else{ console.log("Sorry, no Web storage support!"); }
}
function renderTable(container, rows){
    container.innerHTML = "";
    const table = document.createElement("table");
    table.className = "enrolled-table";
    table.onclick = (e) => {
        e.stopPropagation();
    };

    const thead = table.appendChild(document.createElement("thead"));
    const tr = document.createElement("tr");
    thead.appendChild(tr);

    columns.forEach(
        (meta, columnName) => {
            if(columnName !== "Session") {
                const th = document.createElement("th");
                th.innerHTML = columnName;
                th.id = container.id + "-" + columnName;
                th.onclick = () => {
                    const sortedRows = sortRows(
                        container.id,
                        columnName
                    );
                    renderTable(container, sortedRows);
                };
                tr.appendChild(th);
            }
        }
    );

    table.appendChild(document.createElement("tbody"));
    const tbody = table.querySelector("tbody");
    rows.forEach((row, rowIndex) => {
        const rowHTML = document.createElement("tr");

        columns.forEach((meta, columnName) => {
            if(columnName !== "Session") {
                const td = document.createElement("td");
                td.textContent = row[columnName];
                rowHTML.appendChild(td);
            }
        });

        rowHTML.onclick = () => {
            openModal(container, row, (newVote, newStatus) => {
                row.Voto = newVote;
                row.Stato = newStatus;

                const key = "rows-" + container.id;
                const data = JSON.parse(sessionStorage.getItem(key));
                data.students[rowIndex] = row;
                sessionStorage.setItem(key, JSON.stringify(data));

                renderTable(container, data.students);
            });
        };


        tbody.appendChild(rowHTML);
    });
    if(enableMultiInsert(rows)){
        // Bottone inserimento multiplo
        const multiInsertBtn = document.createElement("button");
        multiInsertBtn.textContent = "INSERIMENTO MULTIPLO";
        multiInsertBtn.onclick = () => openMultiInsertModal(container, rows);
        container.appendChild(multiInsertBtn);
    }
    container.appendChild(table);
}
// END TABLE MANAGEMENT FUNCTION

// BEGIN SORTING FUNCTION
function sort(rows, column, sortState) {
    const colMeta = columns.get(column);
    if (!colMeta) return rows;

    const dir = sortState.direction === "asc" ? 1 : -1;

    return rows.sort((a, b) => {
        const v1 = a[column];
        const v2 = b[column];

        if (colMeta.type === "number") {
            return dir * (v1 - v2);
        }

        return dir * v1.localeCompare(v2, "it", { sensitivity: "base" });
    });
}
function toggleState(sortState, column){
    // toggle
    if (sortState.column === column) {
        sortState.direction =
            sortState.direction === "asc" ? "desc" : "asc";
    } else {
        sortState.column = column;
        sortState.direction = "asc";
    }
    return sortState;
}
function sortRows(id, column){
    const key = "rows-"+id;
    const sortState = toggleState(
        JSON.parse(sessionStorage.getItem(key))["sortState"],
        column
    );
    updateSessionStorageSortStates(key, sortState);
    const sortedRows = sort(
        JSON.parse(sessionStorage.getItem(key))["students"],
        column, sortState);
    updateSessionStorageStudents(key, sortedRows);
    console.log(sortedRows);
    console.log(sortState);
    return sortedRows;
}
// END SORTING FUNCTION

// BEGIN SESSION STORAGE MANAGEMENT FUNCTIONS
function updateSessionStorage(key, value){
    sessionStorage.setItem(key,value);
}
function updateSessionStorageSortStates(key, sortState){
    let values = JSON.parse(sessionStorage.getItem(key));
    values["sortState"] = sortState;
    updateSessionStorage(key,JSON.stringify(values));
}
function updateSessionStorageStudents(key, students){
    const values = JSON.parse(sessionStorage.getItem(key));
    values["students"] = students;
    updateSessionStorage(key,JSON.stringify(values));
}
// END SESSION STORAGE MANAGEMENT FUNCTIONS

// BEGIN VOTE MODIFICATION MANAGEMENT
function openModal(container, row, onSave) {
    // overlay (oscura)
    const overlay = document.createElement("div");
    overlay.className = "table-overlay";

    // modal
    const modal = document.createElement("div");
    modal.className = "table-modal";

    const h3 = document.createElement("h3");
    h3.textContent = "Modifica esito";
    modal.appendChild(h3);

    buildModal(modal).then(() => {
        document.getElementById("saveBtn").onclick = () => {
            const newVote = modal.querySelector("#voteInput").value;
            const newStatus = modal.querySelector("#statusInput").value;

            onSave(newVote, newStatus);
            updateExamResult({
                "examId": Number(row["Session"]),
                "studentId": row["Matricola"],
                "grade": newVote,
                "status": newStatus
            }).then(() => overlay.remove());
        };

        document.getElementById("cancelBtn").onclick = () => {
            overlay.remove();
        };
    });

    overlay.appendChild(modal);
    document.body.appendChild(overlay);
}
function updateExamResult(payload) {
    return fetch("/api/updateExamResult", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(payload)
    }).then(res => {
        if (!res.ok)
            throw new Error("Errore aggiornamento esito");
    }).catch(
        err => console.error(err)
    );
}
async function buildModal(modal) {

    // VOTO
    const gradesRes = await fetch("/api/examGrades");
    const grades = await gradesRes.json();

    const voteSelect = document.createElement("select");
    voteSelect.id ="voteInput";
    const voteLabel = document.createElement("label");
    voteLabel.textContent = "Voto";
    voteLabel.setAttribute("for", voteSelect.id);
    modal.appendChild(voteLabel);
    grades.forEach(g => {
        const opt = document.createElement("option");
        opt.textContent = g.label;
        voteSelect.appendChild(opt);
    });
    modal.appendChild(voteSelect);

    // STATO
    const statusRes = await fetch("/api/examStatus");
    const statuses = await statusRes.json();

    const statusSelect = document.createElement("select");
    statusSelect.id ="statusInput";
    const statusLabel = document.createElement("label");
    statusLabel.textContent = "Stato";
    statusLabel.setAttribute("for", statusSelect.id);
    modal.appendChild(statusLabel);

    statuses.forEach(s => {
        const opt = document.createElement("option");
        opt.textContent = s.label;
        statusSelect.appendChild(opt);
    });
    modal.appendChild(statusSelect);

    // ACTIONS (QUI sei sicuro che tutto sopra è pronto)
    const actions = document.createElement("div");
    actions.className = "modal-actions";

    const saveBtn = document.createElement("button");
    saveBtn.id = "saveBtn";
    saveBtn.textContent = "Salva";

    const cancelBtn = document.createElement("button");
    cancelBtn.id = "cancelBtn";
    cancelBtn.textContent = "Annulla";

    actions.appendChild(saveBtn);
    actions.appendChild(cancelBtn);
    modal.appendChild(actions);
}
function enableMultiInsert(rows){
    let enable = false;
    let count = 0;
    rows.forEach(r => {
        if(r.Stato === "non inserito") {
            enable = true;
            count++;
        }
    });
    return enable && count > 1;
}
function openMultiInsertModal(container, rows) {
    // Filtra solo le righe "non inserite"
    const rowsToInsert = rows.filter(r => {
        return r.Stato === "non inserito" || r.Voto === "\<vuoto\>";
    });

    if(rowsToInsert.length === 0) {
        alert("Non ci sono studenti da inserire");
        return;
    }

    // Overlay oscura
    const overlay = document.createElement("div");
    overlay.className = "table-overlay";

    // Modal
    const modal = document.createElement("div");
    modal.className = "table-modal";

    // Header
    modal.innerHTML = `<h3>Inserimento multiplo</h3>`;

    // Tabella dentro modal
    const table = document.createElement("table");
    table.className = "multi-insert-table";
    const thead = table.appendChild(document.createElement("thead"));
    const trHead = document.createElement("tr");
    ["Matricola","Nome","Cognome","Voto"].forEach(col => {
        const th = document.createElement("th");
        th.textContent = col;
        trHead.appendChild(th);
    });
    thead.appendChild(trHead);

    const tbody = table.appendChild(document.createElement("tbody"));
    rowsToInsert.forEach((row, idx) => {
        const tr = document.createElement("tr");
        ["Matricola","Nome","Cognome"].forEach(c => {
            const td = document.createElement("td");
            td.textContent = row[c];
            tr.appendChild(td);
        });
        const tdInput = document.createElement("td");
        const input = document.createElement("input");
        input.type = "number";
        input.min = 0;
        input.max = 30;
        input.dataset.rowIndex = idx; // memorizza indice
        tdInput.appendChild(input);
        tr.appendChild(tdInput);
        tbody.appendChild(tr);
    });

    modal.appendChild(table);

    // Bottoni azioni
    const actionsDiv = document.createElement("div");
    actionsDiv.className = "modal-actions";

    const sendBtn = document.createElement("button");
    sendBtn.textContent = "INVIA";
    sendBtn.onclick = () => {
        const payloads = [];
        tbody.querySelectorAll("input").forEach((inp, i) => {
            if(inp.value) {
                const row = rowsToInsert[i];
                payloads.push({
                    examId: Number(row["Session"]),
                    studentId: row["Matricola"],
                    grade: Number(inp.value),
                    status: "verbalizzato"
                });

                // Aggiorna lo stato della riga locale
                row.Stato = "verbalizzato";
                row.Voto = Number(inp.value);
            }
        });

        if(payloads.length > 0) {
            updateMultipleExamResults(payloads)
                .then(() => {
                    overlay.remove();
                    // aggiorna tabella principale
                    renderTable(container, rows);
                })
                .catch(err => alert("Errore invio voti: " + err));
        } else {
            alert("Inserire almeno un voto.");
        }
    };

    const cancelBtn = document.createElement("button");
    cancelBtn.textContent = "Annulla";
    cancelBtn.onclick = () => overlay.remove();

    actionsDiv.appendChild(sendBtn);
    actionsDiv.appendChild(cancelBtn);
    modal.appendChild(actionsDiv);

    overlay.appendChild(modal);
    document.body.appendChild(overlay);
}
function updateMultipleExamResults(payloads) {
    return fetch("/api/updateExamResult", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payloads)
    }).then(res => {
        if(!res.ok) throw new Error("Errore server");
        return res.json();
    });
}
// END VOTE MODIFICATION MANAGEMENT

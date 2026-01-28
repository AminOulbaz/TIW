const columns = new Map([
    ["Matricola", {
        type: "string",
        accessor: s => s.studentId
    }],
    ["Cognome", {
        type: "string",
        accessor: s => s.surname
    }],
    ["Nome", {
        type: "string",
        accessor: s => s.name
    }],
    ["Email", {
        type: "string",
        accessor: s => s.email
    }],
    ["Corso", {
        type: "string",
        accessor: s => s.degreeProgramCode
    }],
    ["Voto", {
        type: "string",
        accessor: s => s.grade
    }],
    ["Stato", {
        type: "string",
        accessor: s => s.status
    }],
    ["Sessione", {
        type: "number",
        accessor: s => s.examId
    }]
]);
let examGrades;
let examsStatuses;

document.addEventListener("DOMContentLoaded", () => {
    //recupero dei dati del professore: nome e cognome
    fetch("/api/professor/me",{
        method: "POST"
    })
        .then(res => res.json())
        .then(professor => {
            document.getElementById("welcome").textContent =
                "Benvenuto Prof. " +professor.surname  +" "+  professor.name;
        });

    // recupero dei voti
    fetch("/api/examGrades")
        .then(res => res.json())
        .then(data => examGrades = data);

    // recupero degli stati di valutazione
    fetch("/api/examStatus")
        .then(res => res.json())
        .then(data => examsStatuses = data);

    // recupero dei corsi e degli appelli associati ai corsi
    fetch("/api/courses")
        .then(res => res.json())
        .then(data => renderCourses(data));

    //recupero dei verbali generati dal professore
    getVerbals();
});
function logout() {
    fetch("/api/logout",
        { method: "POST" })
        .then(
            () =>
                window.location.href = "/html/login.html"
        );
}

// BEGIN RENDERING MANAGEMENT FUNCTION
function renderCourses(courses) {
    const container = document.getElementById("coursesContainer");
    container.innerHTML = "";

    courses.forEach(c => {

        // course box
        const courseBox = document.createElement("div");
        courseBox.className = "course-box";
        courseBox.textContent = `${c.name} (${c.code})`;
        courseBox.onclick = () => toggleBox("sessions-" + c.code);

        // crea la session list
        const sessionList = document.createElement("div");
        sessionList.className = "session-list";
        sessionList.id = "sessions-" + c.code;
        sessionList.style.display = "none";

        // inserisce gli appelli nella session list
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
/*
* Il display è importante definirlo in modo corretto per ogni elemento altrimenti la semantica si rompe
* */
function toggleBox(id) {
    const box = document.getElementById(id);
    box.style.display = (box.style.display === "block") ? "none" : "block";
}
function toggleTable(id) {
    const table = document.getElementById(id);
    table.style.display = (table.style.display === "table") ? "none" : "table";
}
function toogleRow(id){
    const row = document.getElementById(id);
    row.style.display = (row.style.display === "table-row") ? "none" : "table-row";
}
function showEnrolledStudents(examSessionElement, examSessionId) {
    let enrolledContainer = examSessionElement.querySelector(".enrolled-container");
    if (enrolledContainer != null) {
        examSessionElement.removeChild(enrolledContainer);
    }

    // contenitore iscritti
    enrolledContainer = document.createElement("div");
    enrolledContainer.className = "enrolled-container";
    enrolledContainer.id = enrolledContainer.className +"-"+ examSessionId;
    enrolledContainer.textContent = "Caricamento iscritti...";
    examSessionElement.appendChild(enrolledContainer);

    fetch(`/api/enrolled?examSessionId=${examSessionId}`)
        .then(res => {
            if (!res.ok) {
                throw new Error("Errore server");
            }
            return res.json();
        })
        .then(students => renderEnrolledStudents(enrolledContainer, students))
        .catch(err => {
            enrolledContainer.textContent = "Errore nel caricamento iscritti";
            console.error(err);
        });
}
function renderEnrolledStudents(container, students) {
    if (students.length === 0) {
        container.textContent = "Nessuno studente iscritto";
        return;
    }

    // storage dei dati degli studenti per l'ordinamento locale e l'aggiornamento senza interagire con il server ogni volta
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
            //per ogni container vengono associate delle righe che rispecchiano gli studenti iscritti
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
            container.innerHTML = "";
            renderTable(container);
        }
    }
    else{ console.log("Sorry, no Web storage support!"); }
}
//pulizia dei valori precedenti per la rigenerazione del container associato alla sessione selezionata
function sanityCheckEnrolledStudents(container){
    const table = container.querySelector(".enrolled-table");
    if(table != null){
        container.removeChild(table);
    }
    const multipleInsertion = container.querySelector(".btn-multi-insertion");
    if(multipleInsertion != null){
        container.removeChild(multipleInsertion);
    }
    const publishExamResult = container.querySelector(".btn-publish");
    if(publishExamResult != null){
        container.removeChild(publishExamResult);
    }
    const verbalizeExamResult = container.querySelector(".btn-verbalize");
    if(verbalizeExamResult != null){
        container.removeChild(verbalizeExamResult);
    }
}
//rendering della tabella degli iscritti(rows) nel container passato come parametro
function renderTable(container){
    // sanity check
    sanityCheckEnrolledStudents(container);

    //generazione della tabella degli iscritti
    table = document.createElement("table");
    table.className = "enrolled-table";
    table.onclick = (e) => {
        e.stopPropagation();
    };

    //generazione delle colonne della tabella degli iscritti
    const thead = table.appendChild(document.createElement("thead"));
    const tr = document.createElement("tr");
    thead.appendChild(tr);
    columns.forEach(
        (meta, columnName) => {
            if(columnName !== "Sessione") {
                const th = document.createElement("th");
                th.textContent = columnName;
                th.id = container.id + "-" + columnName;
                th.onclick = (event) => {
                    event.stopPropagation();
                    sortRows(container.id, columnName);
                    refreshContainer(container.id);
                };
                tr.appendChild(th);
            }
        }
    );
    //colonna di modifica
    const thModify = document.createElement("th");
    thModify.textContent = "Modifica";
    thModify.id = container.id + "-" + thModify.textContent;
    tr.appendChild(thModify);

    //generazione delle righe associate ai risultati della sessione selezionata
    table.appendChild(document.createElement("tbody"));
    const tbody = table.querySelector("tbody");
    const students = getSessionStorageStudents(getKey(container.id));
    students.forEach((row, rowIdx) => {
        const rowHTML = document.createElement("tr");
        columns.forEach((meta, columnName) => {
            if(columnName !== "Sessione") {
                const td = document.createElement("td");
                td.textContent = row[columnName];
                rowHTML.appendChild(td);
            }
        });

        //generazione del pulsante di modifica del risultato
        const td = document.createElement("td");
        const button = document.createElement("button");
        button.textContent = "Modifica";
        button.className =
            row["Stato"] === 'non inserito' || row["Stato"] === 'inserito' ?
                "btn-modifica" : "btn-modifica-disabled";
        button.onclick = (event) => {
            event.stopPropagation();
            openModal(container, row, (newVote, newStatus) => {
                row.Voto = newVote;
                row.Stato = newStatus;

                updateSessionStorageRow(getKey(container.id), row, rowIdx);
                refreshContainer(container.id);
            });
        };
        td.appendChild(button);
        rowHTML.appendChild(td);
        tbody.appendChild(rowHTML);
    });

    //abilitazione delle funzionalita comuni: inserimento multiplo, pubblicazione e verbalizzazione
    if(enableMultiInsert(students)){
        const multiInsertBtn = document.createElement("button");
        multiInsertBtn.textContent = "INSERIMENTO MULTIPLO";
        multiInsertBtn.className = "btn-multi-insertion";
        multiInsertBtn.onclick = (event) => {
            event.stopPropagation();
            openMultiInsertModal(container.id);
        }
        container.appendChild(multiInsertBtn);
    }
    if(enablePublishing((students))){
        const publishBtn = document.createElement("button");
        publishBtn.textContent = "PUBBLICA";
        publishBtn.className = "btn-publish";
        publishBtn.onclick = (event) => {
            event.stopPropagation();
            publishExamResults(container.id);
        }
        container.appendChild(publishBtn);
    }
    if(enableVerbalizing((students))){
        const verbalizeBtn = document.createElement("button");
        verbalizeBtn.textContent = "VERBALIZZA";
        verbalizeBtn.className = "btn-verbalize";
        verbalizeBtn.onclick = (event) => {
            event.stopPropagation();
            verbalizeExamResults(container.id);
        }
        container.appendChild(verbalizeBtn);
    }
    container.appendChild(table);
}
//refresh del container quando i dati degli studenti vengono modificati
function refreshContainer(containerId){
    renderTable(document.getElementById(containerId));
}
function renderVerbals(verbals){
    const verbalsBox = document.getElementById("verbalsContainer");
    verbalsBox.innerHTML = "";
    verbalsBox.className = "course-box";
    verbalsBox.textContent = "Elenco Verbali";

    //tabella dei verbali
    const tableVerbals = document.createElement("table");
    tableVerbals.id = "verbals"
    verbalsBox.onclick = () => toggleTable(tableVerbals.id);
    tableVerbals.style.display = "none";

    const header = ["Codice","Data creazione", "Corso", "Appello"];
    const verbalKeys = ["examVerbalId","creationTimestamp","examDate","courseCode"];
    const detailHeader = ["Nome","Cognome", "Matricola", "Voto"];
    const verbalDetailKeys = ["name","surname","identifier","vote"];
    const thead = document.createElement("thead");
    const tr = document.createElement("tr");
    header.forEach(header =>{
        const th = document.createElement("th");
        th.textContent = header;
        tr.appendChild(th);
    });
    thead.appendChild(tr);
    tableVerbals.appendChild(thead);

    const tbody = document.createElement("tbody");
    verbals.forEach(verbal => {
        //riga principale in cui vengono visualizzati i dati del verbale
        const mainTr = document.createElement("tr");
        mainTr.className = "main-row";
        verbalKeys.forEach(key =>{
           const td = document.createElement("td");
           td.textContent = verbal[key];
           mainTr.appendChild(td);
        });

        //riga nascosta in cui sono contenuti gli esiti verbalizzati associati al verbale principale
        const detailsTr =  document.createElement("tr");
        detailsTr.id = verbal.examVerbalId;
        detailsTr.className = "detail-row";
        detailsTr.style.display = "none";
        mainTr.onclick = (event) => {
          event.stopPropagation();
          toogleRow(detailsTr.id);
        };

        //cella di dimensione pari al numero di colonne in cui verrà inserita la tabella degli studenti
        const detailsTd  = document.createElement("td");
        detailsTd.colSpan = verbalKeys.length;
        const detailsTable = document.createElement("table");
        detailsTable.style.width = "100%";
        detailsTable.style.borderCollapse = "collapse";
        const detailsThead = document.createElement("thead");
        const detailsTheadTr = document.createElement("tr");
        detailHeader.forEach(header => {
            const th = document.createElement("th");
            th.textContent = header;
            detailsTheadTr.appendChild(th);
        });
        detailsThead.appendChild(detailsTheadTr);
        detailsTable.appendChild(detailsThead);
        //inserimento degli studenti
        const detailTbody = document.createElement("tbody");
        verbal.students.forEach(student => {
            const detailTr = document.createElement("tr");
            verbalDetailKeys.forEach(key => {
                const td = document.createElement("td");
                td.textContent = student[key];
                detailTr.appendChild(td);
            });
            detailTbody.appendChild(detailTr);
        });
        detailsTable.appendChild(detailTbody);


        tbody.appendChild(mainTr);
        detailsTd.appendChild(detailsTable);
        detailsTr.appendChild(detailsTd);
        tbody.appendChild(detailsTr);
    });
    tableVerbals.appendChild(tbody);
    verbalsBox.appendChild(tableVerbals);
}
function getVerbals(){
    //recupero dei verbali generati dal professore
    fetch("/api/verbals")
        .then(res => res.json())
        .then(data => renderVerbals(data));
}
// END RENDERING MANAGEMENT FUNCTION

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
    return sortedRows;
}
// END SORTING FUNCTION

// BEGIN SESSION STORAGE MANAGEMENT FUNCTIONS
function updateSessionStorage(key, value){
    sessionStorage.setItem(key,value);
}
function updateSessionStorageSortStates(key, sortState){
    const values = JSON.parse(sessionStorage.getItem(key));
    values["sortState"] = sortState;
    updateSessionStorage(key,JSON.stringify(values));
}
function updateSessionStorageStudents(key, students){
    const values = JSON.parse(sessionStorage.getItem(key));
    values["students"] = students;
    updateSessionStorage(key,JSON.stringify(values));
}
function updateSessionStorageRow(key, row, rowIdx){
    const values = getSessionStorageContainer(key);
    values.students[rowIdx] = row;
    updateSessionStorage(key, JSON.stringify(values));
}
function getSessionStorageContainer(key){
    return JSON.parse(sessionStorage.getItem(key));
}
function getSessionStorageStudents(key){
    return getSessionStorageContainer(key).students;
}
function getKey(containerId){
    return "rows-"+containerId;
}
// END SESSION STORAGE MANAGEMENT FUNCTIONS

// BEGIN VOTE MODIFICATION MANAGEMENT
function openModal(container, row, onSave) {
    // overlay per oscurare lo sfondo sottostante all'interfaccia modale
    const overlay = document.createElement("div");
    overlay.className = "overlay";

    // modal
    const modal = document.createElement("div");
    modal.className = "modal";

    const h3 = document.createElement("h3");
    h3.textContent = "Inserisci esito";
    modal.appendChild(h3);

    buildModal(modal).then(() => {
        document.getElementById("saveBtn").onclick = () => {
            const newVote = modal.querySelector("#voteInput").value;
            const newStatus = "inserito";

            onSave(newVote, newStatus);
            updateExamResult({
                "examId": Number(row.Sessione),
                "studentId": row.Matricola,
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
    const voteSelect = document.createElement("select");
    const voteLabel = document.createElement("label");
    voteSelect.id ="voteInput";
    voteLabel.textContent = "Voto";
    voteLabel.setAttribute("for", voteSelect.id);
    modal.appendChild(voteLabel);
    examGrades.forEach(examGrade => {
        const opt = document.createElement("option");
        opt.value = examGrade;
        opt.textContent = examGrade;
        voteSelect.appendChild(opt);
    });
    modal.appendChild(voteSelect);

    const actions = document.createElement("div");
    actions.className = "modal-actions";
    const saveBtn = document.createElement("button");
    saveBtn.id = "saveBtn";
    saveBtn.textContent = "Salva";
    actions.appendChild(saveBtn);
    const cancelBtn = document.createElement("button");
    cancelBtn.id = "cancelBtn";
    cancelBtn.textContent = "Annulla";
    actions.appendChild(cancelBtn);
    modal.appendChild(actions);
}
function openMultiInsertModal(containerId) {
    // Overlay oscura
    const overlay = document.createElement("div");
    overlay.className = "overlay";
    // Modal
    const modal = document.createElement("div");
    modal.className = "modal";
    // Header
    modal.innerHTML = `<h3>Inserimento multiplo</h3>`;

    // Tabella dove vengono visualizzati le informazioni degli studenti e la select per scegliere il voto da inserire
    //Intestazione della tabella
    const columns = ["Matricola","Nome","Cognome","Email", "Corso","Voto"];
    const table = document.createElement("table");
    table.className = "multi-insert-table";
    const thead = table.appendChild(document.createElement("thead"));
    const trHead = document.createElement("tr");
    columns.forEach(column => {
        const th = document.createElement("th");
        th.textContent = column;
        trHead.appendChild(th);
    });
    thead.appendChild(trHead);

    //corpo della tabella
    const tbody = table.appendChild(document.createElement("tbody"));
    getSessionStorageStudents(getKey(containerId)).forEach((row, rowIdx) => {
        if(row.Stato === "non inserito") {
            const tr = document.createElement("tr");
            columns.forEach((column) => {
                const td = document.createElement("td");
                if (column === "Voto") {
                    const selectVote = document.createElement("select");
                    examGrades.forEach(examGrade => {
                        const opt = document.createElement("option");
                        opt.value = examGrade;
                        opt.textContent = examGrade;
                        selectVote.appendChild(opt);
                    });
                    selectVote.addEventListener("change", e => {
                        // aggiorna il voto associato alla riga
                        row.Voto = e.target.value;
                        row.Stato = row.Voto === "<vuoto>" ? "non inserito" : "inserito";
                        updateSessionStorageRow(getKey(containerId), row, rowIdx);
                    });
                    td.appendChild(selectVote);
                } else
                    td.textContent = row[column];
                tr.appendChild(td);
            });
            tbody.appendChild(tr);
        }
    });
    modal.appendChild(table);

    // Bottoni azioni
    const actionsDiv = document.createElement("div");
    actionsDiv.className = "modal-actions";

    const sendBtn = document.createElement("button");
    sendBtn.textContent = "INSERISCI";
    sendBtn.onclick = () => {
        const payloads = [];
        const filteredRows = getSessionStorageStudents(getKey(containerId)).filter(
            r => r.Stato === "inserito"
        )
        filteredRows.forEach(row => {
            payloads.push(getPayload(row));
        });
        updateMultipleExamResults(
            payloads,"inserimento", filteredRows[0].Sessione).then(() => {
            overlay.remove();
            refreshContainer(containerId);
        });
    };

    const cancelBtn = document.createElement("button");
    cancelBtn.textContent = "ANNULLA";
    cancelBtn.onclick = () => {
        overlay.remove();
    };

    actionsDiv.appendChild(sendBtn);
    actionsDiv.appendChild(cancelBtn);
    modal.appendChild(actionsDiv);

    overlay.appendChild(modal);
    document.body.appendChild(overlay);
}
function publishExamResults(containerId){
    // pubblica solo gli esami di cui il professore ha inserito un esito
    getSessionStorageStudents(getKey(containerId)).forEach((row,rowIdx) => {
      if(row.Stato === "inserito"){
          const rowPublished = row;
          rowPublished.Stato = "pubblicato";
          updateSessionStorageRow(getKey(containerId),rowPublished,rowIdx);
      }
    })
    const filteredRows = getSessionStorageStudents(getKey(containerId)).filter(
        row => row.Stato === "pubblicato"
    )
    const payloads = [];
    filteredRows.forEach(row => payloads.push(getPayload(row)));
    updateMultipleExamResults(payloads,"pubblicazione", filteredRows[0].Sessione).then(() => refreshContainer(containerId));
}
async function verbalizeExamResults(containerId) {
    // verbalizza solo gli esiti che sono stati pubblicati dal professore o rifiutati dallo studente
    getSessionStorageStudents(getKey(containerId)).forEach((row, rowIdx) => {
        if (row.Stato === "pubblicato" || row.Stato === "rifiutato") {
            const rowVerbalized = row;
            rowVerbalized.Stato = "verbalizzato";
            updateSessionStorageRow(getKey(containerId), rowVerbalized, rowIdx);
        }
    })
    const filteredRows = getSessionStorageStudents(getKey(containerId)).filter(
        row => row.Stato === "verbalizzato"
    )
    const payloads = [];
    filteredRows.forEach(row => payloads.push(getPayload(row)));
    try{
        await updateMultipleExamResults(
            payloads,
            "verbalizzazione",
            filteredRows[0].Sessione
        );

        refreshContainer(containerId);
        getVerbals();
    } catch (err) {
        console.error(err);
    }
}
async function updateMultipleExamResults(rawPayload, type, examId) {
    const payload = {
        payload: rawPayload,
        type: type,
        examId: examId
    };

    return fetch("/api/updateExamResults", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(payload)
    }).then(res => {
        if (!res.ok)
            throw new Error("Errore aggiornamento esiti");
        return res;
    });
}
function getPayload(row){
    return {
        "examId": Number(row.Sessione),
        "studentId": row.Matricola,
        "grade": row.Voto,
        "status": row.Stato
    };
}
// END VOTE MODIFICATION MANAGEMENT

// BEGIN ENABLING EVENT MODIFICATION EXAMRESULT STATUS MANAGEMENT
function enableVerbalizing(rows){
    return enablingEvent(rows, "pubblicato") > 0 || enablingEvent(rows, "rifiutato") > 0;
}
function enablePublishing(rows){
    return enablingEvent(rows, "inserito");
}
function enableMultiInsert(rows){
    return enablingEvent(rows, "non inserito") > 1;
}
//funzione che conta le occorenze filtrate da filter
function enablingEvent(rows, filter){
    let count = 0;
    rows.forEach(r => {
        if(r.Stato === filter) {
            count++;
        }
    });
    return count;
}
// END ENABLING EVENT MODIFICATION EXAMRESULT STATUS MANAGEMENT

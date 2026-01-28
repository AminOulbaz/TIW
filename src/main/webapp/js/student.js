const examStore = new Map();
document.addEventListener("DOMContentLoaded", () => {
    //get delle informazioni dello studente
    fetch("/api/student/me")
        .then(r => r.json())
        .then(student => document.getElementById("welcome").textContent = "Benvenuto, "+student.name+" "+student.surname);
    fetch("/api/student/courses")
        .then(r => r.json())
        .then(courses => renderCourses(courses));
});

// BEGIN COURSE CONTAINER MANAGEMENT
function renderCourses(courses) {

    const container = document.getElementById("courses-container");
    container.innerHTML = "";

    for (const course of courses) {

        const courseBox = document.createElement("div");
        courseBox.className = "course-box";
        courseBox.textContent =
            `${course.name} (${course.code}) ${course.credits} CFU`;

        const examsId = `exams-${course.code}`;
        courseBox.onclick = (e) => {
            if (courseBox.classList.contains("btn-disabled")) {
                e.preventDefault();
                return;
            }
            toggleExamList(examsId);
        }

        container.appendChild(courseBox);

        const examList = document.createElement("div");
        examList.className = "exam-list";
        examList.id = examsId;

        fetch("/api/student/examSessions?courseCode="+course.code)
            .then(r => r.json())
            .then(async sessions => {
                for (const session of sessions) {
                    const examEntry = document.createElement("div");
                    examEntry.className = "exam-entry";
                    const examInfo = document.createElement("div");
                    examInfo.className = "exam-exam-info";
                    examInfo.textContent = "Data: " + session.date + " Tipologia: " + session.type + " Aula: " + session.room;
                    const actions = await getActionsForExamEntry(session.id);
                    examInfo.appendChild(actions);
                    examEntry.appendChild(examInfo);
                    examList.appendChild(examEntry);
                }
            });
        container.appendChild(examList);
    }
}
async function getActionsForExamEntry(examEntryId) {
    // gestione delle azioni associati ad ogni azione in base allo stato di valutazione del risultato
    const actions = document.createElement("div");
    actions.className = "exam-actions";
    actions.id = actions.className+"-"+examEntryId;

    const isSubscribed = await fetch(
        "/api/student/isSubscribed?examSessionId="+examEntryId
    ).then(r => r.json());
    if (!isSubscribed) {

        actions.appendChild(createAction(
            "/api/student/subscribe",
            examEntryId,
            "Iscriviti",
            "btn btn-subscribe",
            null,
            "post",
            null,
            actions.id
        ));

    } else {

        const result = await fetch(
            "/api/student/result?examSessionId="+examEntryId
        ).then(r => r.json());

        if (result.status === "non inserito" || result.grade === "<vuoto>") {
            actions.appendChild(createAction(
                "/api/student/unsubscribe",
                examEntryId,
                "Disiscriviti",
                "btn btn-unsubscribe",
                null,
                "post",
                result,
                actions.id
            ));
        }
        else {
            actions.appendChild(createAction(
                "/api/student/result",
                examEntryId,
                "Esito",
                "btn btn-esito",
                null,
                "get",
                result
            ));
        }
    }
    return actions;
}
function createAction(action, examSessionId, label, className, bgColor, method = "post", result = null, id = "") {
    const button = document.createElement("button");
    button.textContent = label;
    button.className = className;
    button.onclick =
        (e) => {
            if (button.classList.contains("btn-disabled")) {
                e.preventDefault();
                return;
            }
            callback(action + "?examSessionId=" + examSessionId, method, result, id).then(() => {});
        }

    if (bgColor) button.style.background = bgColor;
    return button;
}
async function callback(url, method, result, id) {
    try {
        const response = await fetch(url, { method });
        if (response.status === 200) {
            const data = await response.json();
            if (url.includes("unsubscribe")) {
                subscriptionProcessing(
                    id, "/api/student/subscribe", data.examId, "Iscriviti","btn btn-subscribe")
                ;
                createPopup(createParaghraph("Disiscrizione completata"));
            }
            else if (url.includes("subscribe")) {
                subscriptionProcessing(
                    id, "/api/student/unsubscribe", data.examId, "Disiscriviti","btn btn-unsubscribe")
                ;
                createPopup(createParaghraph("Iscrizione effettuata con successo"));
            }
            else if (url.includes("result") && result !== null) {
                examResultProcessing(result);
            }
        }
        else{
            createPopup(createParaghraph("Errore durante l'operazione"));
        }

    } catch (e) {
        createPopup(createParaghraph(e));
    }
}
function toggleExamList(id) {
    const el = document.getElementById(id);
    el.style.display = (el.style.display === "none" || el.style.display === "")
        ? "block"
        : "none";
}
function createParaghraph(content){
    const par = document.createElement("p");
    par.textContent=content;
    return par;
}
function createPopup(elementToShow) {
    document.querySelectorAll(".btn").forEach(
        e => e.classList.add("btn-disabled")
    );
    document.querySelectorAll(".course-box").forEach(
        e => e.classList.add("btn-disabled")
    );

    const popup = document.createElement("div");
    popup.className = "popup";
    popup.appendChild(elementToShow);
    const closePopupBtn = document.createElement("button");
    closePopupBtn.className = "btn";
    closePopupBtn.id = "closePopupBtn";
    closePopupBtn.textContent = "Esci"
    closePopupBtn.onclick = () => closePopup(popup);
    popup.appendChild(closePopupBtn);
    document.body.appendChild(popup);
}
function closePopup(element) {
    element.remove();
    document.querySelectorAll(".btn").forEach(
        e => e.classList.remove("btn-disabled")
    );
    document.querySelectorAll(".course-box").forEach(
        e => e.classList.remove("btn-disabled")
    );
}
function setPopup(element){
    document.querySelectorAll(".btn").forEach(
        e => e.classList.add("btn-disabled")
    );
    document.querySelectorAll(".course-box").forEach(
        e => e.classList.add("btn-disabled")
    );
    element.classList.add("popup");
}

function subscriptionProcessing(id, actionEndPoint,  examId, label, className){
    const action = document.getElementById(id);
    action.innerHTML = "";
    const button = createAction(
        actionEndPoint,
        examId,
        label,
        className,
        null,
        "post",
        null,
        id
    );
    action.appendChild(button);
}
function examResultProcessing(result){
    const statusToShowExamResult = new Set(["pubblicato", "rifiutato", "verbalizzato"]);
    if(statusToShowExamResult.has(result.status) && (result.grade === "30 e lode" || (result.grade >= 18 && result.grade <= 30))){
        const exam_result = document.createElement("div");
        exam_result.appendChild(createParaghraph("Studente: " + result.studentId));
        exam_result.appendChild(createParaghraph("Corso: " + result.course));
        exam_result.appendChild(createParaghraph("Appello: " + result.examId));
        exam_result.appendChild(createParaghraph("Voto: " + result.grade));

        if(result.status === "pubblicato") {
            exam_result.id = "exam-result";
            exam_result.className = "exam-result";
            exam_result.draggable = true;
            // salvataggio del risultato per poterlo recuperare dal target drop
            // key castato a stringa per evitare problemi di associazione indefinita
            examStore.set(String(result.examId), result);

            // catching dell'evento di drag start
            exam_result.addEventListener("dragstart", (e) => {
                e.dataTransfer.setData("text/plain", String(result.examId));
                exam_result.classList.add("dragging");
                /*const img = resultArea.cloneNode(true);
                img.style.position = "absolute";
                img.style.top = "-9999px";
                document.body.appendChild(img);
                e.dataTransfer.setDragImage(img, img.offsetWidth / 2, img.offsetHeight / 2);
                setTimeout(() => document.body.removeChild(img), 0);*/
            });

            // catching dell'evento di drag end
            exam_result.addEventListener("dragend", () => {
                exam_result.classList.remove("dragging");
            });

            // definizione del cestino come target del drop
            const trash = document.getElementById("trash-bin");
            setTrashBinAsTargetToDrop(trash);
            document.getElementById("trash-area").classList.remove("hidden");
            setPopup(exam_result);
            document.body.appendChild(exam_result);
        }
        else {
            exam_result.appendChild(createParaghraph("Stato: " + result.status));
            createPopup(exam_result);
        }
        exam_result.querySelectorAll("p").forEach(e => e.style.textAlign = "center");
        }
    else if(result.status === "inserito"){
        createPopup(createParaghraph("Voto non ancora definito"));
    }
}
// END COURSE CONTAINER MANAGEMENT

// BEGIN REJECTION EXAM RESULT MANAGEMENT
function createRejectPopup(examId){
    const rejectPopup = document.createElement("div");
    rejectPopup.appendChild(createParaghraph("Confermi il rifiuto del voto?"));
    const actions = document.createElement("div");
    actions.name = "popup-actions";

    // CANCELLA
    const cancelBtn = document.createElement("button");
    cancelBtn.textContent = "CANCELLA";
    cancelBtn.onclick = () => closePopup(rejectPopup);

    // CONFERMA
    const confirmBtn = document.createElement("button");
    confirmBtn.textContent = "CONFERMA";
    confirmBtn.onclick = async () => {
        closePopup(rejectPopup);
        await rejectExam(examId);
    };
    actions.appendChild(cancelBtn);
    actions.appendChild(confirmBtn);
    rejectPopup.appendChild(actions);
    setPopup(rejectPopup);
    document.body.appendChild(rejectPopup);
}
function setTrashBinAsTargetToDrop(trash) {
    trash.addEventListener("dragover", e => {
        e.preventDefault();
        trash.classList.add("over");
    });
    trash.addEventListener("dragleave", () => {
        trash.classList.remove("over");
    });
    trash.addEventListener("drop", e => {
        e.preventDefault();
        trash.classList.remove("over");

        const examSessionId = e.dataTransfer.getData("text/plain");
        if(!examSessionId) return;
        const result = examStore.get(examSessionId);
        if(!result) return;

        // aggiornamento dello stato
        examStore.delete(examSessionId);

        //aggiornamento della vista
        closePopup(document.getElementById("exam-result"));
        document.getElementById("trash-area").classList.add("hidden");

        //popup per la scelta di rifiuto del risultato
        createRejectPopup(examSessionId);
    });
}
async function rejectExam(examSessionId) {
    fetch(
        `/api/student/reject?examSessionId=${examSessionId}`,
        { method: "POST" }
    ).then(
        response => {
            if (response.ok)
                createPopup(createParaghraph("Voto rifiutato correttamente"));
        }
    );
}
// END REJECTION EXAM RESULT MANAGEMENT

function logout() {
    fetch("/api/logout",
        {method: "POST" })
        .then(
            () =>
                window.location.href = "/html/login.html"
        );
}

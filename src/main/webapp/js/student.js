document.addEventListener("DOMContentLoaded", async () => {

    const student = await fetch("/api/student/me").then(r => r.json());
    const courses = await fetch("/api/student/courses").then(r => r.json());
    document.getElementById("welcome").textContent =
        `Benvenuto, ${student.name} ${student.surname}`;

    renderCourses(courses);
});

// BEGIN COURSE CONTAINER MANAGEMENT
async function renderCourses(courses) {

    const container = document.getElementById("courses-container");
    container.innerHTML = "";

    for (const course of courses) {

        const courseBox = document.createElement("div");
        courseBox.className = "course-box";
        courseBox.textContent =
            `${course.name} (${course.code}) ${course.credits} CFU`;

        const examsId = `exams-${course.code}`;
        courseBox.onclick = () => toggleExamList(examsId);

        container.appendChild(courseBox);

        const examList = document.createElement("div");
        examList.className = "exam-list";
        examList.id = examsId;

        const sessions = await fetch(
            `/api/student/examSessions?courseCode=${course.code}`
        ).then(r => r.json());

        for (const session of sessions) {
            examList.appendChild(
                await renderExamSession(session)
            );
        }

        container.appendChild(examList);
    }
}

async function renderExamSession(session) {

    const enrolled = await fetch(
        `/api/student/isEnrolled?examSessionId=${session.id}`
    ).then(r => r.json());
    console.log(enrolled);

    const result = await fetch(
        `/api/student/result?examSessionId=${session.id}`
    ).then(r => r.json());

    const wrapper = document.createElement("div");
    wrapper.className = "exam-entry";

    wrapper.innerHTML = `
        <div class="exam-info">
            <strong>${session.date}</strong>
            ${session.type} Aula: ${session.room}
        </div>
    `;

    const actions = document.createElement("div");
    actions.className = "exam-actions";

    if (!enrolled) {

        actions.appendChild(createForm(
            "subscribe",
            session.id,
            "Iscriviti",
            "btn btn-subscribe"
        ));

    } else {

        if (result.status === "NOTINSERTED" && result.grade === "EMPTY") {
            actions.appendChild(createForm(
                "unsubscribe",
                session.id,
                "Disiscriviti",
                "btn btn-subscribe",
                "#d9534f"
            ));
        }

        actions.appendChild(createForm(
            "result",
            session.id,
            "Esito",
            "btn btn-esito",
            null,
            "get"
        ));
    }

    wrapper.appendChild(actions);
    return wrapper;
}
function createForm(action, examSessionId, label, className, bgColor, method = "post") {

    const form = document.createElement("form");
    form.action = action;
    form.method = method;

    const input = document.createElement("input");
    input.type = "hidden";
    input.name = "examSessionId";
    input.value = examSessionId;

    const button = document.createElement("button");
    button.type = "submit";
    button.textContent = label;
    button.className = className;

    if (bgColor) button.style.background = bgColor;

    form.append(input, button);
    return form;
}
function toggleExamList(id) {
    const el = document.getElementById(id);
    el.style.display = (el.style.display === "none" || el.style.display === "")
        ? "block"
        : "none";
}
// END COURSE CONTAINER MANAGEMENT

function logout() {
    fetch("/api/logout",
        { method: "POST" })
        .then(
            () =>
                window.location.href = "/html/login.html"
        );
}

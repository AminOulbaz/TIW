document.addEventListener("DOMContentLoaded", () => {

    const form = document.getElementById("loginForm");

    form.addEventListener("submit", e => {
        e.preventDefault();

        const username = document.getElementById("username").value;
        const password = document.getElementById("password").value;

        fetch("/api/login", {
            method: "POST",
            body: new URLSearchParams({ username, password })
        })
        .then(res => {
            if (!res.ok) throw new Error();
            return res.json();
        })
        .then(user => {
            window.location =
                user.role === "PROFESSOR"
                    ? "/html/professor.html"
                    : "/html/student.html";
        })
        .catch(() => {
            document.getElementById("error").textContent =
                "Credenziali non valide";
        });
    });
});

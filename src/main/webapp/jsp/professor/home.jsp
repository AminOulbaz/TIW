<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Home Docente</title>
    <link rel="stylesheet" href="css/professor.css">
    <script>
        function toggleSessions(courseCode) {
            const box = document.getElementById("sessions-" + courseCode);
            box.style.display = (box.style.display === "block") ? "none" : "block";
        }

        function goToIscritti(examSessionId) {
            window.location.href = "enrolled?examSessionId=" + examSessionId;
        }
    </script>
</head>

<body>

<!-- HEADER -->
<div class="header">
    <h1>Benvenuto Professore ${professor.name} ${professor.surname}</h1>
    <div class="logout">
        <a href="logout">Logout</a>
    </div>
</div>

<!-- MAIN CONTENT -->
<div class="container">

    <!-- GESTIONE CORSI -->
    <div class="section">
        <h2>Gestione Corsi</h2>

        <c:forEach var="c" items="${courses}">
            <div class="course-box" onclick="toggleSessions('${c.code}')">
                    ${c.name} (${c.code})
            </div>

            <div class="session-list" id="sessions-${c.code}">
                <c:forEach var="s" items="${examSessionsByCourse[c.code]}">
                    <div class="session-item"
                         onclick="goToIscritti('${s.id}')">
                            ${s.date} - ${s.type}
                    </div>
                </c:forEach>
            </div>
        </c:forEach>
    </div>

    <!-- GESTIONE VERBALI -->
    <div class="section">
        <h2>Gestione Verbali</h2>

        <div class="course-box"
             onclick="window.location.href='verbals'">
            Verbali
        </div>
    </div>


</div>

</body>
</html>

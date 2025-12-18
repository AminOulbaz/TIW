<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Home Studente</title>
    <link rel="stylesheet" href="css/student.css">

    <script>
        function toggleExamList(id) {
            var el = document.getElementById(id);
            el.style.display = (el.style.display === "none" || el.style.display === "") ? "block" : "none";
        }
    </script>
</head>

<body>
<!-- HEADER -->
<div class="header">
    <h1>Benvenuto, ${student.name} ${student.surname}</h1>
    <p><strong>Corso di Laurea:</strong> ${student.degreeProgramCode}</p>
    <div class="logout">
        <a href="logout">Logout</a>
    </div>
</div>

<!-- MAIN CONTENT -->
<div class="container">

    <!-- GESTIONE CORSI -->
    <div class="section">
        <h2>I tuoi corsi</h2>

        <!-- Lista corsi -->
        <c:forEach var="course" items="${courses}">
            <div class="course-box" onclick="toggleExamList('exams-${course.code}')">
                <strong>${course.name}</strong> (${course.code}) ${course.credits} CFU
            </div>

            <!-- Appelli del corso -->
            <div class="exam-list" id="exams-${course.code}">
                <c:forEach var="session" items="${examSessionsByCourse[course.code]}">
                    <div class="exam-entry">
                        <div class="exam-info">
                            <strong>${session.date}</strong> ${session.type} Aula: ${session.room}
                        </div>

                        <div class="exam-actions">
                            <!-- Controllo iscrizione -->
                            <c:choose>

                                <c:when test="${not enrollments.contains(session.id)}">
                                    <div>
                                        <form action="subscribe" method="post">
                                            <input type="hidden" name="examSessionId" value="${session.id}">
                                            <button class="btn btn-subscribe" type="submit">Iscriviti</button>
                                        </form>
                                    </div>
                                </c:when>

                                <c:otherwise>

                                    <c:set var="result" value="${examResultsBySession[session.id]}" />

                                    <c:if test="${result.status == 'NOTINSERTED' and result.grade == 'EMPTY'}">
                                        <div>
                                            <form action="unsubscribe" method="post">
                                                <input type="hidden" name="examSessionId" value="${session.id}">
                                                <button class="btn btn-subscribe" type="submit" style="background:#d9534f;">
                                                    Disiscriviti
                                                </button>
                                            </form>
                                        </div>
                                    </c:if>
                                    <div>
                                        <form action="result" method="get">
                                            <input type="hidden" name="examSessionId" value="${session.id}">
                                            <button class="btn btn-esito" type="submit">Esito</button>
                                        </form>
                                    </div>
                                </c:otherwise>

                            </c:choose>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:forEach>
    </div>
</div>
</body>
</html>

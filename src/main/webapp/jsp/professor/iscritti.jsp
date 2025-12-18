<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Iscritti all'appello</title>
    <link rel="stylesheet" href="css/professor.css">
</head>

<body>

<!-- HEADER -->
<div class="header">
    <h1>Iscritti all'appello</h1>
    <div class="home">
        <a href="home">Home</a>
    </div>
</div>

<div class="container">

    <!-- CARD INFO APPELLO -->
    <div class="card">
        <h2>Appello: ${examSession.courseCode}</h2>
        <p class="info"><strong>Data:</strong> ${examSession.date}</p>
        <p class="info"><strong>Tipo:</strong> ${examSession.type}</p>

        <!-- Bottoni PUBBLICA e VERBALIZZA -->
        <div class="actions">
            <c:if test="${hasInserted}">
                <form action="publish" method="post" style="display:inline;">
                    <input type="hidden" name="examSessionId" value="${examSession.id}">
                    <input type="hidden" name="infos" value="${infos}">
                    <button class="btn-action btn-publish" type="submit">PUBBLICA</button>
                </form>
            </c:if>

            <c:if test="${hasVerbalizable}">
                <form action="verbalize" method="post" style="display:inline;">
                    <input type="hidden" name="examSessionId" value="${examSession.id}">
                    <button class="btn-action btn-verbalizza" type="submit">VERBALIZZA</button>
                </form>
            </c:if>
        </div>

        <!-- Tabella iscritti -->
        <table>
            <thead>
            <tr>
                <c:set var="nextOrd" value="${ord == 'ASC' ? 'DESC' : 'ASC'}" />
                <%
                String[] keys = {"student_id","last_name","first_name","email","degree_program_code","grade","status"};
                String[] values = {"Matricola","Cognome","Nome","Email","Corso di laurea","Voto","Stato"};
                for(int i = 0; i < keys.length; i++){
                    String param = keys[i];
                    String value = values[i];
                %>
                    <th><a class="order-link" href="?examSessionId=${examSession.id}&sort=<%= param%>&ord=${nextOrd}"><%= value%></a></th>
                <%
                }
                %>
                <th>Modifica</th>
            </tr>
            </thead>

            <tbody>
            <c:forEach var="info" items="${infos}">
                <tr>
                    <td>${info.student.id}</td>
                    <td>${info.student.surname}</td>
                    <td>${info.student.name}</td>
                    <td>${info.student.email}</td>
                    <td>${info.student.degreeProgramCode}</td>
                    <td>${info.examResult.grade.label}</td>
                    <td>${info.examResult.status.label}</td>
                    <td>
                        <form action="modify" method="get">
                            <input type="hidden" name="studentId" value="${info.student.id}">
                            <input type="hidden" name="examSessionId" value="${info.examResult.examId}">
                            <button type=${info.examResult.status.label == 'non inserito' || info.examResult.status.label == 'inserito' ? 'submit' : 'button'}
                                    class=${info.examResult.status.label == 'non inserito' || info.examResult.status.label == 'inserito' ? "btn-modifica" : "btn-modifica-disabled"}>Modifica</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

</div>
</body>
</html>

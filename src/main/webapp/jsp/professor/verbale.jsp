<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Verbale Appello</title>
    <link rel="stylesheet" href="css/professor.css">
</head>

<body>

<!-- HEADER -->
<div class="header">
    <h1>Verbale Appello</h1>
    <div class="home">
        <a href="home">Home</a>
    </div>
</div>

<div class="container">

    <!-- CARD VERBALE -->
    <div class="card">
        <h2>Verbale: ${verbal.examVerbalId}</h2>

        <p class="info"><strong>Data/Ora creazione:</strong> ${verbal.creationTimestamp}</p>
        <p class="info"><strong>Corso:</strong> ${verbal.examSession.courseCode}</p>
        <p class="info"><strong>Data appello:</strong> ${verbal.examSession.date}</p>
        <p class="info"><strong>Tipo appello:</strong> ${verbal.examSession.type}</p>

        <!-- Tabella studenti verbalizzati -->
        <h3>Esiti verbalizzati</h3>
        <table>
            <thead>
            <tr>
                <th>Nome</th>
                <th>Cognome</th>
                <th>Matricola</th>
                <th>Voto</th>
            </tr>
            </thead>

            <tbody>
            <c:forEach var="info" items="${verbal.examResultWithStudents}">
                <tr>
                    <td>${info.student.name}</td>
                    <td>${info.student.surname}</td>
                    <td>${info.student.id}</td>
                    <td>${info.examResult.grade.label}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

</body>
</html>

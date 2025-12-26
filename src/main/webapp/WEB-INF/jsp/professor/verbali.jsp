<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>I miei verbali</title>
    <link rel="stylesheet" href="css/professor.css">
    <!-- stesso CSS/header delle altre pagine -->
</head>

<body>
<div class="header">
    <h1>Verbali creati</h1>
    <div class="home">
        <a href="home">Home</a>
    </div>
</div>

<div class="container">
    <div class="card">
        <h2>Elenco verbali</h2>

        <table>
            <thead>
            <tr>
                <th>Codice</th>
                <th>Data creazione</th>
                <th>Corso</th>
                <th>Appello</th>
            </tr>
            </thead>

            <tbody>
            <c:forEach var="v" items="${verbals}">
                <tr onclick="location.href='/verbal?verbalId=${v.examVerbalId}'"
                    style="cursor:pointer">
                    <td>${v.examVerbalId}</td>
                    <td>${v.creationTimestamp}</td>
                    <td>${v.examSession.courseCode}</td>
                    <td>${v.examSession.date}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

    </div>
</div>
</body>
</html>

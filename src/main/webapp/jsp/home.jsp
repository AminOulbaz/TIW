<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Home</title>
</head>
<body>

<h2>Benvenuto, ${user.username}!</h2>

<p>Ruolo: <strong>${user.role}</strong></p>

<hr>

<h3>Funzionalità disponibili</h3>

<ul>

    <!-- FUNZIONI PER STUDENTE -->
    <c:if test="${user.role == 'STUDENT'}">

        <li><a href="${pageContext.request.contextPath}/visualizzaCorsi">
            Visualizza corsi disponibili
        </a></li>

        <li><a href="${pageContext.request.contextPath}/iscrizioneEsami">
            Iscriviti agli esami
        </a></li>

        <li><a href="${pageContext.request.contextPath}/storicoPrenotazioni">
            Visualizza storico prenotazioni
        </a></li>
    </c:if>

    <!-- FUNZIONI PER DOCENTE -->
    <c:if test="${user.role == 'PROFESSOR'}">

        <li><a href="${pageContext.request.contextPath}/gestioneAppelli">
            Gestisci appelli
        </a></li>

        <li><a href="${pageContext.request.contextPath}/gestioneIscritti">
            Visualizza iscritti agli appelli
        </a></li>

        <li><a href="${pageContext.request.contextPath}/pubblicaRisultati">
            Pubblica risultati
        </a></li>
    </c:if>

</ul>

<hr>

<a href="${pageContext.request.contextPath}/logout">Logout</a>

</body>
</html>

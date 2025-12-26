<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Esito Esame</title>
  <link rel="stylesheet" href="css/student.css">
</head>

<body>

<!-- HEADER -->
<div class="header">
  <h2>Esito esame</h2>
  <div class="home">
    <a href="home">Home</a>
  </div>
</div>

<!-- MAIN CONTENT -->
<div class="container">
  <c:choose>

    <c:when test="${examResult.status.label == 'non inserito'}">
      <p><strong>Voto non ancora definito</strong></p>
    </c:when>

    <c:otherwise>

      <div class="box">
        <p><strong>Studente:</strong> ${student.name} ${student.surname}</p>
        <p><strong>Corso:</strong> ${course.name} (${course.code})</p>
        <p><strong>Appello:</strong> ${examSession.date} (${examSession.type})</p>
        <p><strong>Voto:</strong> ${examResult.grade.label}</p>

        <c:if test="${examResult.status.label == 'rifiutato'}">
          <p class="danger">Voto rifiutato</p>
        </c:if>

        <c:if test="${examResult.status.label == 'pubblicato'
                           and examResult.grade.label != 'riprovato'
                           and examResult.grade.label != 'assente'}">

          <form action="refuse" method="post">
            <input type="hidden" name="examSessionId" value="${examSession.id}">
            <button class="btn btn-refuse" type="submit">RIFIUTA</button>
          </form>

        </c:if>

      </div>

    </c:otherwise>

  </c:choose>
</div>

</body>
</html>

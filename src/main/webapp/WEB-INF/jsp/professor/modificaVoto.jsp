<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Modifica Voto</title>
  <link rel="stylesheet" href="css/professor.css">
</head>

<body>

<!-- HEADER -->
<div class="header">
  <h1>Modifica Voto</h1>
  <div class="home">
    <a href="home">Home</a>
  </div>
</div>

<!-- MAIN -->
<div class="container">
  <div class="card">

    <h2>
      ${info.student.surname} ${info.student.name}
    </h2>

    <form action="modify" method="post">

      <input type="hidden" name="studentId" value="${info.student.id}">
      <input type="hidden" name="examSessionId" value="${examSession.id}">

      <p class="info">
        <strong>Email:</strong> ${info.student.email}
      </p>

      <p class="info">
        <strong>Corso di laurea:</strong> ${info.student.degreeProgramCode}
      </p>

      <label for="grade">Voto</label>
      <select name="grade" id="grade">
        <c:forEach var="g" items="${grades}">
          <option value="${g.label}">
              ${g.label == "<vuoto>" ? "&lt;vuoto&gt;" : g.label}
          </option>
        </c:forEach>
      </select>

      <div class="actions">
        <button type="button" class="btn-cancel"
                onclick="window.history.back()">
          Annulla
        </button>

        <button type="submit" class="btn-save">
          Inserisci
        </button>
      </div>

    </form>

  </div>
</div>

</body>
</html>


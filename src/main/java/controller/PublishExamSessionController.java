package controller;

import dao.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ExamResult;
import model.ExamResultWithStudent;
import model.ExamStatus;
import model.Student;
import service.ProfessorService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/publish")
public class PublishExamSessionController extends HttpServlet {
    ProfessorService professorService;
    @Override
    public void init() throws ServletException {
        String url = getServletContext().getInitParameter("db.url");
        String user = getServletContext().getInitParameter("db.user");
        String pwd = getServletContext().getInitParameter("db.password");

        try {
            Connection connection = DriverManager.getConnection(url, user, pwd);
            professorService = new ProfessorService(
                    new ProfessorDaoImpl(connection),
                    new CourseDaoImpl(connection),
                    new ExamSessionDaoImpl(connection),
                    new ExamResultDaoImpl(connection),
                    new VerbalDaoImpl(connection)
            );
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int examSessionId = Integer.parseInt(req.getParameter("examSessionId"));
        List<ExamResultWithStudent> examResultsWithStudent = (List<ExamResultWithStudent>) req.getSession().getAttribute("infos");
        for(ExamResultWithStudent examResultWithStudent : examResultsWithStudent) {
            if(examResultWithStudent.getExamResult().getStatus().equals(ExamStatus.INSERTED)) {
                examResultWithStudent.getExamResult().setStatus(ExamStatus.PUBLISHED);
                professorService.updateExamResult(examResultWithStudent.getExamResult());
            }
        }
        List<ExamResultWithStudent> examResultWithStudents = professorService.getExamResultWithStudentsByExamSessionId(examSessionId);
        req.setAttribute("infos", examResultWithStudents);
        req.setAttribute("examSession", professorService.getExamSessionByExamSessionId(examSessionId));
        req.setAttribute("hasInserted", examResultWithStudents
                .stream().anyMatch(e -> e.getExamResult().getStatus().equals(ExamStatus.INSERTED))
        );
        req.setAttribute("hasVerbalizable", examResultWithStudents
                .stream().anyMatch(e -> e.getExamResult().getStatus().equals(ExamStatus.PUBLISHED))
        );
        req.getRequestDispatcher("/jsp/professor/iscritti.jsp")
                .forward(req, resp);
    }
}

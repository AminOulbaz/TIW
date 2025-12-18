package controller;

import dao.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.*;
import service.ProfessorService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/modify")
public class ModifyExamResultController extends HttpServlet {
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int examSessionId = Integer.parseInt(req.getParameter("examSessionId"));
        req.setAttribute("info", professorService.getExamResultWithStudent(
                examSessionId , req.getParameter("studentId")
        ));
        req.setAttribute(
                "examSession" , professorService.getExamSessionByExamSessionId(examSessionId)
        );
        req.setAttribute(
                "grades" , ExamGrade.getExamGrades()
        );

        req.getRequestDispatcher("/jsp/professor/modificaVoto.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int examSessionId = Integer.parseInt(req.getParameter("examSessionId"));
        ExamResult examResult = new ExamResult();
        examResult.setExamId(examSessionId);
        examResult.setStudentId((req.getParameter("studentId")));
        examResult.setGrade(
                ExamGrade.getExamGrade(req.getParameter("grade"))
        );
        examResult.setStatus(ExamStatus.INSERTED);
        professorService.updateExamResult(examResult);
        resp.sendRedirect("/enrolled?examSessionId=" + examSessionId);
    }
}

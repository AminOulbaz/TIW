package web;

import dao.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.*;
import service.ProfessorService;
import service.ProfessorServiceFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebServlet("/modify")
public class ModifyExamResultController extends HttpServlet {
    ProfessorService professorService;

    @Override
    public void init() throws ServletException {
        try {
            professorService = ProfessorServiceFactory.getProfessorService(getServletContext());
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

        req.getRequestDispatcher("/WEB-INF/jsp/professor/modificaVoto.jsp")
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

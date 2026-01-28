package web;

import dao.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ExamStatus;
import model.User;
import model.Verbal;
import service.ProfessorService;
import service.ProfessorServiceFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebServlet("/verbal")
public class VerbalizedExamSessionController extends HttpServlet {
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
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        Verbal verbal = professorService.getVerbalById(
                req.getParameter("verbalId")
        );
        verbal.setExamSession(professorService.getExamSessionByExamSessionId(
                verbal.getExamSession().getId()
        ));

        verbal.setExamResultWithStudents(
                professorService.getExamResultWithStudentsByExamSessionId(
                        verbal.getExamSession().getId()
                ).stream().filter(e -> e.getExamResult().getStatus().equals(ExamStatus.VERBALIZED)).toList()
        );
        req.setAttribute("verbal", verbal);
        req.getRequestDispatcher("/WEB-INF/jsp/professor/verbale.jsp")
                .forward(req, resp);
    }
}

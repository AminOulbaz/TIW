package controller;

import dao.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ExamResult;
import model.ExamStatus;
import model.User;
import model.Verbal;
import service.ProfessorService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/verbal")
public class VerbalizedExamSessionController extends HttpServlet {
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
        req.getRequestDispatcher("/jsp/professor/verbale.jsp")
                .forward(req, resp);
    }
}

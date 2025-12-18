package controller;

import dao.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Professor;
import model.User;
import model.Verbal;
import service.ProfessorService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/verbals")
public class VerbalizedExamSessionsController extends HttpServlet {
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if(req.getSession().getAttribute("user") != null) {
            Professor professor =
                    professorService.getProfessorByUsername(
                            ((User) req.getSession().getAttribute("user")).getUsername());

            List<Verbal> verbals = professorService.getAllVerbalsOrdered(professor.getId());

            for(Verbal verbal : verbals) {
                verbal.setExamSession(
                        professorService.getExamSessionByExamSessionId(verbal.getExamSession().getId())
                );
            }

            req.setAttribute("verbals", verbals);

            req.getRequestDispatcher("/jsp/professor/verbali.jsp")
                    .forward(req, resp);
        }
    }
}

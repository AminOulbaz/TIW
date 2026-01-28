package web;

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
import service.ProfessorServiceFactory;

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
        try {
            professorService = ProfessorServiceFactory.getProfessorService(getServletContext());
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

            req.getRequestDispatcher("/WEB-INF/jsp/professor/verbali.jsp")
                    .forward(req, resp);
        }
    }
}

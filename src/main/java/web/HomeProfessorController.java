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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/homeProfessor")
public class HomeProfessorController extends HttpServlet {
    ProfessorService professorService;
    public HomeProfessorController(){}

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
        req.setAttribute("professor", professorService.getProfessorByUsername(user.getUsername()));
        req.setAttribute("courses",professorService.getCoursesByProfessor(
                professorService.getProfessorByUsername(user.getUsername()).getId())
        );
        Map<String, List<ExamSession>> examSessionsByCourse = new HashMap<>();
        for(Course c : professorService.getCoursesByProfessor(
                professorService.getProfessorByUsername(user.getUsername()).getId())){
            examSessionsByCourse.put(c.getCode(), professorService.getExamSessionsByCourse(c.getCode()));
        }
        req.setAttribute("examSessionsByCourse", examSessionsByCourse);
        req.getRequestDispatcher("/WEB-INF/jsp/"+user.getRole().homePage()+".jsp")
                .forward(req, resp);
    }
}

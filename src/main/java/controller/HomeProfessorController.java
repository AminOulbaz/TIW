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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/homeProfessor")
public class HomeProfessorController extends HttpServlet {
    ProfessorService professorService;
    public HomeProfessorController(){}

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
        req.getRequestDispatcher("/jsp/"+user.getRole().homePage()+".jsp")
                .forward(req, resp);
    }
}

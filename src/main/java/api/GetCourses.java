package api;

import com.google.gson.Gson;
import dao.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Course;
import model.User;
import service.ProfessorService;
import service.ProfessorServiceFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/courses")
public class GetCourses extends HttpServlet {
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
        User user = (User) req.getSession(false).getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/html/login.html");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        List<Course> courses = professorService.getCoursesByProfessor(
                professorService.getProfessorByUsername(user.getUsername()).getId()
        );
        courses.forEach(course -> {
            course.setExamSessions(
                    professorService.getExamSessionsByCourse(course.getCode())
            );
        });

        String coursesJson = new Gson().toJson(courses);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(coursesJson);
    }
}


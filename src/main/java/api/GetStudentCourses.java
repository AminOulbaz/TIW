package api;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Course;
import model.Student;
import model.User;
import service.StudentService;
import service.StudentServiceFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/student/courses")
public class GetStudentCourses extends HttpServlet {
    private StudentService studentService;
    @Override
    public void init() throws ServletException {
        try {
            studentService = StudentServiceFactory.getStudentService(getServletContext());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        Student student = studentService.getStudentByUsername(user.getUsername());
        resp.setContentType("application/json");
        resp.getWriter().write(
                new Gson().toJson(
                        studentService.getCoursesByDegreeCode(student.getDegreeProgramCode())
                ));
    }
}

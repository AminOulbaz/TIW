package api;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.StudentService;
import service.StudentServiceFactory;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/api/student/examSessions")
public class GetExamSessions extends HttpServlet {
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
        String courseCode = req.getParameter("courseCode");
        resp.setContentType("application/json");
        resp.getWriter().write(
                new Gson().toJson(
                        studentService.getExamSessionByCourseId(courseCode)
                )
        );
    }
}

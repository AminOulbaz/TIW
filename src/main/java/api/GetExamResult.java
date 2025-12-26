package api;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import json.ExamResultsGsonFactory;
import model.Student;
import model.User;
import service.StudentService;
import service.StudentServiceFactory;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/api/student/result")
public class GetExamResult extends HttpServlet {
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
                ExamResultsGsonFactory.getGson().toJson(
                        studentService.getExamResultByExamSessionAndStudentId(
                                Integer.parseInt(req.getParameter("examSessionId")),
                                student.getId()
                        )
                )
        );
    }
}

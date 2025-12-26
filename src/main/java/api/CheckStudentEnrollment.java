package api;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import service.StudentService;
import service.StudentServiceFactory;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/api/student/isEnrolled")
public class CheckStudentEnrollment  extends HttpServlet {
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
        System.out.println("isEnrolled() ->");
        User user = (User) req.getSession().getAttribute("user");
        resp.setContentType("application/json");
        boolean isEnrolled = studentService.isSubscribed(
                studentService.getStudentByUsername(
                        user.getUsername()
                ).getId(),
                Integer.parseInt(req.getParameter("examSessionId"))
        );
        String isEnrolledJson = new Gson().toJson(isEnrolled);
        System.out.println(isEnrolled+" "+isEnrolledJson);
        resp.getWriter().write(isEnrolledJson);
    }
}

package api;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Student;
import model.User;
import service.StudentService;
import service.StudentServiceFactory;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/api/student/subscribe")
public class SubscribeExamSession extends HttpServlet {
    StudentService studentService;

    @Override
    public void init() throws ServletException {
        try {
            studentService = StudentServiceFactory.getStudentService(getServletContext());
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user != null) {
            int examSessionId = Integer.parseInt(req.getParameter("examSessionId"));

            studentService.subscribeStudentToExamSession(
                    studentService.getStudentByUsername(
                            user.getUsername()
                    ).getId()
                    , examSessionId
            );
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"examId\":" + examSessionId + "}");
        }
    }
}


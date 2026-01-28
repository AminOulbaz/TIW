package api;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ExamResult;
import model.ExamStatus;
import model.User;
import service.StudentService;
import service.StudentServiceFactory;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/api/student/reject")
public class RejectExamResult extends HttpServlet {
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

            ExamResult examResult  =studentService.getExamResultByExamSessionAndStudentId(
                    examSessionId, studentService.getStudentByUsername(user.getUsername()).getId()
            );
            examResult.setStatus(ExamStatus.REFUSED);
            studentService.refuseExamResult(examResult);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"examId\":" + examSessionId + "}");
        }
    }
}

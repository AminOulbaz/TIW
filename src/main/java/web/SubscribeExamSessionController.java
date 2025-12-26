package web;

import dao.CourseDaoImpl;
import dao.ExamResultDaoImpl;
import dao.ExamSessionDaoImpl;
import dao.StudentDaoImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Student;
import service.StudentService;
import service.StudentServiceFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebServlet("/subscribe")
public class SubscribeExamSessionController extends HttpServlet {
    StudentService studentService;
    public SubscribeExamSessionController(){}

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
        Student student = (Student) req.getSession().getAttribute("student");
        Integer examSessionId = Integer.parseInt(req.getParameter("examSessionId"));

        if (student == null)
            resp.sendRedirect(req.getContextPath() + "/login");

        studentService.subscribeStudentToExamSession(
                student.getId(), examSessionId
        );

        resp.sendRedirect(req.getContextPath() + "/home");
    }
}

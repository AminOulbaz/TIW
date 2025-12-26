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
import model.*;
import service.StudentService;
import service.StudentServiceFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebServlet("/unsubscribe")
public class UnsubscribeExamSessionController extends HttpServlet {
    StudentService studentService;
    public UnsubscribeExamSessionController(){}

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
        Integer examSessionId = Integer.parseInt(req.getParameter("examSessionId"));

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        studentService.unsubscribeStudentToExamSession(studentService.getStudentByUsername(user.getUsername()).getId(), examSessionId);
        resp.sendRedirect(req.getContextPath() + "/home");
    }
}



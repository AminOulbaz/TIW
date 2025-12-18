package controller;

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

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet("/unsubscribe")
public class UnsubscribeExamSessionController extends HttpServlet {
    StudentService studentService;
    public UnsubscribeExamSessionController(){}

    @Override
    public void init() throws ServletException {
        String url = getServletContext().getInitParameter("db.url");
        String user = getServletContext().getInitParameter("db.user");
        String pwd = getServletContext().getInitParameter("db.password");

        try {
            Connection connection = DriverManager.getConnection(url, user, pwd);
            studentService = new StudentService(
                    new StudentDaoImpl(connection),
                    new CourseDaoImpl(connection),
                    new ExamSessionDaoImpl(connection),
                    new ExamResultDaoImpl(connection)
            );
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



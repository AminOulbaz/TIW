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
import model.ExamResult;
import model.Student;
import service.StudentService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebServlet("/refuse")
public class RefuseExamResultController extends HttpServlet {

    private StudentService studentService;

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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Student student = (Student) req.getSession().getAttribute("student");
        int examSessionId = Integer.parseInt(req.getParameter("examSessionId"));

        studentService.refuseExamResult(
                studentService.getExamResultByExamSessionAndStudentId(
                        examSessionId, student.getId()
                )
        );

        resp.sendRedirect(req.getContextPath()
                + "/result?examSessionId=" + examSessionId);
    }
}

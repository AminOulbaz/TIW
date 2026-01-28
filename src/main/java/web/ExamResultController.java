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
import model.Course;
import model.ExamResult;
import model.ExamSession;
import model.Student;
import service.ProfessorServiceFactory;
import service.StudentService;
import service.StudentServiceFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebServlet("/result")
public class ExamResultController extends HttpServlet {

    private StudentService studentService;

    @Override
    public void init() throws ServletException {
        try {
            studentService = StudentServiceFactory.getStudentService(getServletContext());
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Student student = (Student) req.getSession().getAttribute("student");
        int examSessionId = Integer.parseInt(req.getParameter("examSessionId"));

        ExamResult examResult =
                studentService.getExamResultByExamSessionAndStudentId(examSessionId,student.getId());

        ExamSession session =
                studentService.getExamSessionByExamSessionId(examSessionId);

        Course course =
                studentService.getCourseByCourseCode(session.getCourseCode());

        req.setAttribute("examResult", examResult);
        req.setAttribute("student", student);
        req.setAttribute("examSession", session);
        req.setAttribute("course", course);

        req.getRequestDispatcher("/WEB-INF/jsp/student/esito.jsp")
                .forward(req, resp);
    }
}

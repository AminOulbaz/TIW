package web;

import dao.*;
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
import java.util.*;
import java.util.stream.Collectors;

@WebServlet("/homeStudent")
public class HomeStudentController extends HttpServlet {
    StudentService studentService;
    public HomeStudentController(){}

    @Override
    public void init() throws ServletException {
        try {
            studentService = StudentServiceFactory.getStudentService(getServletContext());
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        Student student = studentService.getStudentByUsername(user.getUsername());
        req.getSession().setAttribute("student", student);

        List<Course> courses = studentService.getCoursesByDegreeCode(student.getDegreeProgramCode());
        req.setAttribute("courses", courses);

        Map<String, List<ExamSession>> examSessionsByCourse = new HashMap<>();
        for(Course c : courses){
            examSessionsByCourse.put(c.getCode(), studentService.getExamSessionByCourseId(c.getCode()));
        }
        req.setAttribute("examSessionsByCourse", examSessionsByCourse);
        Set<Integer> enrollments = studentService.getExamResultsByStudentId(student.getId()).stream()
                        .map(e -> e.getExamId()).collect(Collectors.toSet());
        req.setAttribute("enrollments", enrollments);

        Map<Integer,ExamResult> examResultsBySession = new HashMap<>();
        for(Integer examSessionId : enrollments){
            examResultsBySession.put(examSessionId, studentService.getExamResultByExamSessionAndStudentId(examSessionId, student.getId()));
        }
        req.setAttribute("examResultsBySession", examResultsBySession);


        req.getRequestDispatcher("/WEB-INF/jsp/"+user.getRole().homePage()+".jsp")
                .forward(req, resp);
    }
}


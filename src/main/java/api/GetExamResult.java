package api;

import com.google.gson.Gson;
import dto.ExamResultDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ExamResult;
import dto.ExamResultDto;
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
        ExamResult examResultModel = studentService.getExamResultByExamSessionAndStudentId(
                Integer.parseInt(req.getParameter("examSessionId")),
                student.getId()
        );
        ExamResultDto examResultDto = new ExamResultDto();
        examResultDto.setExamId(examResultModel.getExamId());
        examResultDto.setStudentId(examResultModel.getStudentId());
        examResultDto.setGrade(examResultModel.getGrade().getLabel());
        examResultDto.setStatus(examResultModel.getStatus().getLabel());
        examResultDto.setCourse(
                studentService.getCourseByCourseCode(
                    studentService.getExamSessionByExamSessionId(
                            examResultModel.getExamId()
                    ).getCourseCode()
                ).getName()
        );
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        resp.getWriter().write(new Gson().toJson(examResultDto));
    }
}

package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.*;
import dto.ExamResultDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import json.ExamGradeSerializer;
import json.ExamResultsGsonFactory;
import json.ExamStatusSerializer;
import json.UserSerializer;
import model.*;
import org.eclipse.tags.shaded.org.apache.xalan.serialize.Serializer;
import service.ProfessorService;
import service.ProfessorServiceFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/enrolled")
public class GetEnrolledStudents extends HttpServlet {
    ProfessorService professorService;

    @Override
    public void init() throws ServletException {
        try {
            professorService = ProfessorServiceFactory.getProfessorService(getServletContext());
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int examSessionId = Integer.parseInt(req.getParameter("examSessionId"));
        List<ExamResultDto> examResults =
                professorService.getExamResultDtoWithStudentsByExamSessionId(examSessionId);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(new Gson().toJson(examResults));
    }
}

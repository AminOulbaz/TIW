package api;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dto.ExamResultDto;
import model.ExamGrade;
import model.ExamStatus;
import service.ProfessorService;
import service.ProfessorServiceFactory;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/api/updateExamResult")
public class UpdateExamResult extends HttpServlet {
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            ExamResultDto payload = new Gson().fromJson(req.getReader(), ExamResultDto.class);
            System.out.println(payload);
            model.ExamResult examResult = new model.ExamResult();
            examResult.setExamId(payload.getExamId());
            examResult.setStudentId(payload.getStudentId());
            examResult.setGrade(ExamGrade.getExamGrade(payload.getGrade()));
            examResult.setStatus(ExamStatus.getExamStatus(payload.getStatus()));
            professorService.updateExamResult(examResult);
            System.out.println(examResult);
        } catch (JsonSyntaxException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "JSON invalido");
        }

    }
}

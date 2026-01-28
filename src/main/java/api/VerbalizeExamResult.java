package api;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dto.ExamResultDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ExamGrade;
import model.ExamResult;
import model.ExamStatus;
import model.User;
import service.ProfessorService;
import service.ProfessorServiceFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/professor/verbalize")
public class VerbalizeExamResult extends HttpServlet {
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
        User user = (User) req.getSession().getAttribute("user");
        if (user != null) {
            List<ExamResult> examResults = professorService.getExamResultsByExamSessionId(
                    Integer.parseInt(req.getParameter("examSessionId"))
            );
            for (ExamResult examResult : examResults) {
                if(examResult.getStatus().equals(ExamStatus.PUBLISHED) ||
                        examResult.getStatus().equals(ExamStatus.REFUSED)){
                    examResult.setStatus(ExamStatus.VERBALIZED);
                    professorService.updateExamResult(examResult);
                }
            }

            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
        }
        resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}


package api;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import dto.ExamResultDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ExamGrade;
import model.ExamStatus;
import model.Professor;
import model.User;
import service.ProfessorService;
import service.ProfessorServiceFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/updateExamResults")
public class UpdateExamResults extends HttpServlet {
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
        if (user == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else {
            try {
                JsonElement element = JsonParser.parseReader(req.getReader());
                JsonObject obj = element.getAsJsonObject();

                // definizione del tipo List<ExamResultDto> per deserializzare
                Type listType = new TypeToken<List<ExamResultDto>>() {}.getType();
                List<ExamResultDto> payloadList = new Gson().fromJson(obj.get("payload"), listType);
                for (ExamResultDto payload : payloadList) {
                    model.ExamResult examResult = new model.ExamResult();
                    examResult.setExamId(payload.getExamId());
                    examResult.setStudentId(payload.getStudentId());
                    examResult.setGrade(ExamGrade.getExamGrade(payload.getGrade()));
                    examResult.setStatus(ExamStatus.getExamStatus(payload.getStatus()));

                    professorService.updateExamResult(examResult);
                    System.out.println(examResult);
                }
                if ("verbalizzazione".equals(obj.get("type").getAsString())) {
                    Professor professor = professorService.getProfessorByUsername(user.getUsername());
                    professorService.createVerbal(
                            professorService.makeVerbal(
                                    professor, obj.get("examId").getAsInt()
                            )
                    );
                }
                resp.setStatus(HttpServletResponse.SC_OK);
            } catch (JsonSyntaxException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "JSON invalido");
            }
        }

    }
}

package api;

import com.google.gson.Gson;
import dto.StudentVerbalDto;
import dto.VerbalDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.*;
import service.ProfessorService;
import service.ProfessorServiceFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/verbals")
public class GetVerbals extends HttpServlet {
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
        User user = (User) req.getSession().getAttribute("user");
        if (user == null)
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        Professor professor = professorService.getProfessorByUsername(user.getUsername());
        //per la serializzazione dei verbali
        List<VerbalDto> verbalsDto = new ArrayList<>();
        //popolamento dei verbali con i dati necessari e aggiunta dei verbalDto nei verbali da serializzare
        List<Verbal> verbals = professorService.getAllVerbalsOrdered(professor.getId());
        for(Verbal verbal : verbals) {
            verbal.setExamSession(
                    professorService.getExamSessionByExamSessionId(verbal.getExamSession().getId())
            );
            verbal.setExamResultWithStudents(
                    professorService.getExamResultWithStudentsByExamSessionId(
                            verbal.getExamSession().getId()
                    ).stream().filter(e -> e.getExamResult().getStatus().equals(ExamStatus.VERBALIZED)).toList()
            );
            VerbalDto verbalDto = new VerbalDto();
            verbalDto.setExamVerbalId(verbal.getExamVerbalId());
            verbalDto.setCreationTimestamp(verbal.getCreationTimestamp().toString());
            verbalDto.setExamDate(verbal.getExamSession().getDate().toString());
            verbalDto.setCourseCode(verbal.getExamSession().getCourseCode());
            List<StudentVerbalDto> students = getStudentVerbalDto(verbal.getExamResultWithStudents());
            verbalDto.setStudents(students);
            verbalsDto.add(verbalDto);
        }

        String json = new Gson().toJson(verbalsDto);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);
    }

    private List<StudentVerbalDto> getStudentVerbalDto(List<ExamResultWithStudent> results) {
        List<StudentVerbalDto> students = new ArrayList<>();
        for(ExamResultWithStudent examResultWithStudent: results){
            StudentVerbalDto studentVerbalDto = new StudentVerbalDto();
            studentVerbalDto.setIdentifier(examResultWithStudent.getStudent().getId());
            studentVerbalDto.setName(examResultWithStudent.getStudent().getName());
            studentVerbalDto.setSurname(examResultWithStudent.getStudent().getSurname());
            studentVerbalDto.setVote(examResultWithStudent.getExamResult().getGrade().getLabel());
            students.add(studentVerbalDto);
        }
        return students;
    }
}

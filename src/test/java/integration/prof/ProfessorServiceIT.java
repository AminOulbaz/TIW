package integration.prof;

import dao.*;
import integration.init.TestInitialization;
import model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.ProfessorService;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class ProfessorServiceIT {

    private static Connection connection;
    private static ProfessorDao professorDao;
    private static CourseDao courseDao;
    private static ExamSessionDao examSessionDao;
    private static ExamResultDao examResultDao;
    private static VerbalDao verbalDao;
    private static ProfessorService professorService;

    @BeforeAll
    static void initDb() throws Exception {
        connection = DriverManager.getConnection(
                "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1"
        );

        professorDao = new ProfessorDaoImpl(connection);
        courseDao = new CourseDaoImpl(connection);
        examSessionDao = new ExamSessionDaoImpl(connection);
        examResultDao = new ExamResultDaoImpl(connection);
        verbalDao = new VerbalDaoImpl(connection);
        professorService = new ProfessorService(
                professorDao,
                courseDao,
                examSessionDao,
                examResultDao,
                verbalDao
        );

        TestInitialization.createSchema(connection);
        TestInitialization.insertTestUsers(connection);
    }

    @Test
    void docente_puo_accedere_alla_piattaforma(){
        Professor professor = professorService.getProfessorByUsername("prof01@test.com");
        assertEquals("prof01",professor.getId());
        assertEquals("John",professor.getName());
        assertEquals("Doe",professor.getSurname());
        assertEquals("john.doe@university.com",professor.getEmail());
        assertEquals("Department of Computer Science",professor.getDepartment());


        assertTrue(professorService.getExamResultWithStudentsByExamSessionId(1)
                .stream().anyMatch(e -> e.getExamResult().getStatus().equals(ExamStatus.INSERTED)));


    }
    @Test
    void docente_puo_visualizzare_i_suoi_corsi() {
        List<Course> corsi = professorService.getCoursesByProfessor("prof01");

        assertEquals(3, corsi.size());
        Course course = corsi.stream().filter(
                c -> c.getCode().equals("CS101")
        ).findFirst().orElse(null);
        assertNotNull(course);
        assertEquals("Object Oriented Programming",course.getName());
        assertEquals(12, course.getCredits());
        assertEquals("L-31",course.getDegreeProgramId());
    }
    @Test
    void docente_puo_scegliere_una_data_di_appello(){
        List<ExamSession> appealList = professorService.getExamSessionsByCourse("CS101");
        assertEquals(3, appealList.size());
        ExamSession examSession = appealList.stream().findFirst().orElse(null);
        assertNotNull(examSession);
    }

    void docente_modifica_un_voto_ad_uno_studente(int exam_session_id, String student_id, ExamGrade grade, ExamStatus status) {
        ExamResult examResult = professorService.getExamResultByExamSessionIdAndStudentId(
                exam_session_id, student_id);
        examResult.setGrade(grade);
        examResult.setStatus(status);
        professorService.updateExamResult(examResult);
    }
    @Test
    void docente_puo_modificare_esito_e_stato_di_valutazione_ad_un_iscritto(){
        ExamResult examResult = professorService.getExamResultsByExamSessionId(1).stream().filter(
                e -> e.getStatus().equals(ExamStatus.NOTINSERTED))
                .findFirst().orElse(null);

        docente_modifica_un_voto_ad_uno_studente(
                examResult.getExamId(),
                examResult.getStudentId(),
                ExamGrade.GRADE_18,
                ExamStatus.INSERTED
        );

        ExamResult examResult1 =
                professorService.getExamResultsByExamSessionId(examResult.getExamId()).stream().
            filter(e -> e.getGrade().equals(ExamGrade.GRADE_18) && e.getStatus().equals(ExamStatus.INSERTED)).findFirst().orElse(null);


        assertEquals(examResult.getExamId(), examResult1.getExamId());
        assertEquals(examResult.getStudentId(), examResult1.getStudentId());
    }
    @Test
    void docente_puo_pubblicare_gli_esiti_inseriti(){
        List<ExamResult> examResults = professorService.getExamResultsByExamSessionId(6);
        assertEquals(3, examResults.size());

        List<ExamResult> examResultsModified = examResults.stream()
                .map(
                        e -> {
                            if(e.getStatus().equals(ExamStatus.INSERTED) || e.getStatus().equals(ExamStatus.REFUSED)){
                                return e.getExamResultWithDifferentStatus(ExamStatus.PUBLISHED);
                            }else{
                                return e;
                            }
                        })
                .filter(e -> e.getStatus().equals(ExamStatus.PUBLISHED))
                .toList();

        for(ExamResult examResultModified : examResultsModified){
            professorService.updateExamResult(examResultModified);
        }
        examResultsModified = professorService.getExamResultsByExamSessionId(6);

        for(ExamResult examResult : examResults){
            for(ExamResult examResult1 :examResultsModified){
                if(examResult.getStudentId().equals(examResult1.getStudentId()) &&
                   !examResult.getStatus().equals(examResult1.getStatus()))
                    assertEquals(examResult1.getStatus(), ExamStatus.PUBLISHED);
            }
        }
    }
    @Test
    void docente_puo_verbalizzare_gli_esiti_di_un_appello(){
        List<ExamResult> examResults = professorService.getExamResultsByExamSessionId(2);
        assertEquals(4, examResults.size());
        boolean gradesVerbalized = false;
        for(ExamResult examResult : examResults) {
            if(examResult.getStatus().equals(ExamStatus.REFUSED) ||
                    examResult.getStatus().equals(ExamStatus.PUBLISHED)) {
                gradesVerbalized = true;
                docente_modifica_un_voto_ad_uno_studente(
                        examResult.getExamId(),
                        examResult.getStudentId(),
                        examResult.getStatus().equals(ExamStatus.PUBLISHED) ?
                                examResult.getGrade() : ExamGrade.DEFERRED,
                        ExamStatus.VERBALIZED
                );
            }
        }
        assertTrue(gradesVerbalized);
        Verbal verbal = new Verbal();
        verbal.setCreationTimestamp(
                Timestamp.valueOf(LocalDateTime.now())
        );
        verbal.setProfessorId("prof01");
        verbal.setExamSession(
                professorService.getExamSessionByExamSessionId(2)
        );
        verbal.setExamVerbalId(
                Verbal.generateCode(
                        verbal.getExamSession().getId(),
                        verbal.getProfessorId(),
                        verbal.getCreationTimestamp()
                )
        );
        professorService.createVerbal(verbal);
        System.out.println(verbal);
        Verbal verbalInserted = professorService.getVerbalById(
                verbal.getExamVerbalId()
        );
        System.out.println(verbalInserted);
        assertEquals(
                verbal.getProfessorId(), verbalInserted.getProfessorId()
        );assertEquals(
                verbal.getCreationTimestamp().getTime(), verbalInserted.getCreationTimestamp().getTime()
        );
    }
}

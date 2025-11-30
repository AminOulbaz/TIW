package integration.prof;

import dao.*;
import integration.init.TestInitialization;
import model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.ProfessorService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProfessorServiceIT {

    private static Connection connection;
    private static ProfessorDao professorDao;
    private static CourseDao courseDao;
    private static ExamSessionDao examSessionDao;
    private static ExamEnrollmentStudentDao examEnrollmentStudentDao;
    private static ExamResultDao examResultDao;
    private static ProfessorService professorService;

    @BeforeAll
    static void initDb() throws Exception {
        connection = DriverManager.getConnection(
                "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1"
        );

        professorDao = new ProfessorDaoImpl(connection);
        courseDao = new CourseDaoImpl(connection);
        examSessionDao = new ExamSessionDaoImpl(connection);
        examEnrollmentStudentDao = new ExamEnrollmentStudentDaoImpl(connection);
        examResultDao = new ExamResultDaoImpl(connection);
        professorService = new ProfessorService(
                professorDao,
                courseDao,
                examSessionDao,
                examEnrollmentStudentDao,
                examResultDao
        );

        TestInitialization.createSchema(connection);
        TestInitialization.insertTestUsers(connection);
    }

    @Test
    void docente_puo_vedere_i_suoi_corsi() {
        // setup: dati inseriti in H2 nel @BeforeAll
        List<Course> corsi = professorService.getCoursesByProfessor("prof01");

        assertEquals(2, corsi.size());
        assertFalse(corsi.stream().anyMatch(c -> c.getName().equals("Analisi 1")));
        assertTrue(corsi.stream().anyMatch(c -> c.getName().equals("Object Oriented Programming")));
    }
    @Test
    void docente_puo_vedere_iscritti_a_un_appello() {
        List<ExamEnrollment> iscritti = professorService.getEnrolledStudentByExamSession(1);

        assertEquals(3, iscritti.size());
        //assertTrue(iscritti.stream().anyMatch(s -> s.getEmail().equals("a@studenti.it")));
    }
    @Test
    void docente_puo_vedere_appelli_di_un_corso() {
        List<ExamSession> appealList = professorService.getExamSessionsByCourse("CS101");

        assertEquals(3, appealList.size());
    }
    @Test
    void docente_puo_inserire_un_voto(){
        ExamResult examResult = professorService.getExamResultByExamSessionIdAndStudentId(
                1, "stud01");
        assertEquals(examResult.getGrade(), ExamGrade.EMPTY);
        assertEquals(examResult.getStatus(), ExamStatus.NOTINSERTED);
        examResult.setGrade(ExamGrade.GRADE_18);
        examResult.setStatus(ExamStatus.INSERTED);
        professorService.updateExamResult(examResult);
        examResult = professorService.getExamResultByExamSessionIdAndStudentId(
                examResult.getExamId(),examResult.getStudentId()
        );
        assertEquals(examResult.getGrade(), ExamGrade.GRADE_18);
        assertEquals(examResult.getStatus(), ExamStatus.INSERTED);

    }
    @Test
    void docente_puo_pubblicare_gli_esiti_di_un_appello(){

    }
    @Test
    void docente_puo_verbalizzare_gli_esiti_di_un_appello(){}
}

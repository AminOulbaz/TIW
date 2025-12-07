package integration.stud;

import dao.*;
import integration.init.TestInitialization;
import model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.ProfessorService;
import service.StudentService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class StudentServiceIT {
    private static Connection connection;
    private static StudentDao studentDao;
    private static CourseDao courseDao;
    private static ExamSessionDao examSessionDao;
    private static ExamResultDao examResultDao;
    private static StudentService studentService;

    @BeforeAll
    static void initDb() throws Exception {
        connection = DriverManager.getConnection(
                "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1"
        );
        studentDao = new StudentDaoImpl(connection);
        courseDao = new CourseDaoImpl(connection);
        examSessionDao = new ExamSessionDaoImpl(connection);
        examResultDao = new ExamResultDaoImpl(connection);

        studentService = new StudentService(
                studentDao,courseDao,examSessionDao,examResultDao
        );

        TestInitialization.createSchema(connection);
        TestInitialization.insertTestUsers(connection);
    }

    @Test
    void studente_puo_accedere_alla_piattaforma(){
        Student student = studentService.getStudentByUsername("stud01@test.com");
        assertEquals("stud01",student.getId());
        assertEquals("Mark",student.getName());
        assertEquals("Johnson",student.getSurname());
        assertEquals("mark.johnson@student.com",student.getEmail());
        assertEquals("L-31",student.getDegreeProgramCode());
        assertEquals(2,student.getYearOfStudy());

    }
    @Test
    void studente_puo_vedere_i_corsi_del_suo_programma_di_laurea(){
        List<Student> students = studentService.getStudents();
        Student student = students.get(new Random().nextInt(students.size()));
        assertNotNull(student);
        List<Course> courses = studentService.getCoursesByDegreeCode(student.getDegreeProgramCode());
        for(Course c : courses){
            Course tmpCourse = courseDao.getCourseByCourseId(c.getCode());
            assertEquals(tmpCourse.getDegreeProgramCode(),c.getDegreeProgramCode());
            assertEquals(tmpCourse.getProfessorId(),c.getProfessorId());
            assertEquals(tmpCourse.getName(),c.getName());
            assertEquals(tmpCourse.getCredits(),c.getCredits());
        }
    }
    @Test
    void studente_puo_vedere_gli_appelli_di_un_corso_in_cui_e_iscritto(){
        List<ExamSession> examSessions = studentService.getExamSessionByCourseId("CS101");
        assertEquals(3,examSessions.size());
    }
    @Test
    void studente_puo_visualizzare_esito_di_un_appello(){
        assertNull(studentService.getExamResultNotificationByExamSessionAndStudentId(5,"stud01"));
        assertEquals(
                studentService.getExamResultNotificationByExamSessionAndStudentId(1,"stud01"),
                "Voto non ancora definito");
    }
    @Test
    void studente_puo_rifiutare_esito_di_un_appello(){
        assertFalse(
                studentService.getExamResultByExamSessionAndStudentId(5,"stud01")
                .getStatus().equals(ExamStatus.REFUSED)
        );
        studentService.refuseExamResult(
                studentService.getExamResultByExamSessionAndStudentId(5,"stud01")
        );
        assertTrue(studentService.getExamResultByExamSessionAndStudentId(5,"stud01")
                .getStatus().equals(ExamStatus.REFUSED)
        );
    }
}

package integration.stud;

import dao.*;
import integration.init.TestInitialization;
import model.Student;
import org.junit.jupiter.api.BeforeAll;
import service.ProfessorService;

import java.sql.Connection;
import java.sql.DriverManager;

public class StudentServiceIT {
    private static Connection connection;
    private static StudentDao studentDao;
    private static CourseDao courseDao;
    private static ExamSessionDao examSessionDao;
    private static ExamEnrollmentStudentDao examEnrollmentStudentDao;
    private static ExamResultDao examResultDao;

    @BeforeAll
    static void initDb() throws Exception {
        connection = DriverManager.getConnection(
                "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1"
        );
        courseDao = new CourseDaoImpl(connection);
        examSessionDao = new ExamSessionDaoImpl(connection);
        examEnrollmentStudentDao = new ExamEnrollmentStudentDaoImpl(connection);
        examResultDao = new ExamResultDaoImpl(connection);

        TestInitialization.createSchema(connection);
        TestInitialization.insertTestUsers(connection);
    }
}

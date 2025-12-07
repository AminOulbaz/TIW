package integration.auth;

import static org.junit.jupiter.api.Assertions.*;

import dao.UserDao;
import dao.UserDaoImpl;
import service.AuthService;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AuthServiceIT {

    private static Connection conn;
    private static UserDao userDao;
    private static AuthService auth;

    @BeforeAll
    static void initDb() throws Exception {
        conn = DriverManager.getConnection(
                "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1"
        );

        userDao = new UserDaoImpl(conn);
        auth = new AuthService(userDao);

        createSchema();
        insertTestUsers();
    }

    private static void createSchema() throws SQLException {
        conn.createStatement().execute("""
            CREATE TABLE users (
                username VARCHAR(255) PRIMARY KEY,
                email VARCHAR(255) NOT NULL,
                password VARCHAR(255) NOT NULL,
                role VARCHAR(20) NOT NULL
            );
        """);
    }

    private static void insertTestUsers() throws SQLException {
        conn.createStatement().execute("""
            INSERT INTO users(username,email, password, role) VALUES
            ('studente@test.com','studente@test.com', '1234', 'STUDENT'),
            ('prof@test.com','prof@test.com', 'abcd', 'TEACHER');
        """);
    }

    @Test
    void user_interaction(){
        auth.register("yo","ie","oi@oi.io","STUDENT");
        assertTrue(auth.login("yo","ie"));
        auth.updatePassword("yo","passwordseria");
        assertTrue(auth.login("yo","passwordseria"));
    }

    @Test
    void loginStudente_ok() {
        assertTrue(auth.login("studente@test.com", "1234"));
    }

    @Test
    void loginProfessore_ok() {
        assertTrue(auth.login("prof@test.com", "abcd"));
    }

    @Test
    void login_fallisce_se_passwordErrata() {
        assertFalse(auth.login("studente@test.com", "wrong"));
    }

    @Test
    void login_fallisce_se_utente_non_esiste() {
        assertFalse(auth.login("nessuno@test.com", "pass"));
    }
}

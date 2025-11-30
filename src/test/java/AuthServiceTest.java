import model.StudentRole;
import model.User;
import dao.UserDao;
import service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock UserDao dao;
    @InjectMocks AuthService service;

    @Test
    void shouldAuthenticateValidUser() {
        User u = new User("amin", "password123");
        when(dao.findByUsername("amin")).thenReturn(u);
        assertTrue(service.login("amin", "password123"));
    }

    @Test
    void shouldFailOnWrongPassword() {
        User u = new User("amin", "password123");
        when(dao.findByUsername("amin")).thenReturn(u);
        assertFalse(service.login("amin", "wrong"));
    }

    @Test
    void shouldFailIfUserNotFound() {
        when(dao.findByUsername("ghost")).thenReturn(null);
        assertFalse(service.login("ghost", "whatever"));
    }

    @Test
    void studentRoleRedirectsToStudentHome() {
        User u = new User("amin","pwd", new StudentRole());
        assertEquals("/student/home", u.getHomePage());
    }
}


import it.polimi.progetto.bean.UserBean;
import it.polimi.progetto.dao.UserDAO;
import it.polimi.progetto.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    UserDao dao;
    @InjectMocks AuthService service;

    @Test
    void shouldAuthenticateValidUser() {
        UserBean u = new UserBean("amin", "password123");
        when(dao.findByUsername("amin")).thenReturn(Optional.of(u));

        boolean result = service.login("amin", "password123");
        assertTrue(result);
    }

    @Test
    void shouldFailOnWrongPassword() {
        User u = new User("amin", "password123");
        when(dao.findByUsername("amin")).thenReturn(Optional.of(u));

        assertFalse(service.login("amin", "wrong"));
    }

    @Test
    void shouldFailIfUserNotFound() {
        when(dao.findByUsername("ghost")).thenReturn(Optional.empty());
        assertFalse(service.login("ghost", "whatever"));
    }
}


import web.LoginController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import service.AuthService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoginControllerTest {

    private LoginController controller;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private AuthService authService;

    @BeforeEach
    void setup() {
        authService = mock(AuthService.class);
        controller = new LoginController();
        controller.setAuthService(authService);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void shouldRedirectToDashboardOnSuccess() throws Exception {
        when(authService.login("amin", "password123")).thenReturn(true);
        request.setParameter("username", "amin");
        request.setParameter("password", "password123");

        controller.doPost(request, response);

        assertEquals("/dashboard", response.getRedirectedUrl());
    }

    @Test
    void shouldReturnToLoginOnFailure() throws Exception {
        when(authService.login("amin", "wrong")).thenReturn(false);
        request.setParameter("username", "amin");
        request.setParameter("password", "wrong");

        controller.doPost(request, response);

        assertEquals("/login?error=true", response.getRedirectedUrl());
    }
}

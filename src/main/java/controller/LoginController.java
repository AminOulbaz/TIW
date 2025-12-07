package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AuthService;

import java.io.IOException;

@WebServlet("/login")
public class LoginController extends HttpServlet {

    private AuthService authService;

    @Override
    public void init() throws ServletException {
        super.init();
    }

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        boolean ok = authService.login(username, password);

        if (ok) {
            resp.sendRedirect("/dashboard");
        } else {
            resp.sendRedirect("/login?error=true");
        }
    }
}


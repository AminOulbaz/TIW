package api;

import dao.UserDaoImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import json.UserGsonFactory;
import model.User;
import service.AuthService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebServlet("/api/login")
public class Login extends HttpServlet {
    private AuthService authService;

    @Override
    public void init() throws ServletException {
        String url = getServletContext().getInitParameter("db.url");
        String user = getServletContext().getInitParameter("db.user");
        String pwd = getServletContext().getInitParameter("db.password");

        try {
            Connection connection = DriverManager.getConnection(url, user, pwd);
            this.authService = new AuthService(new UserDaoImpl(connection));
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (authService.login(username, password)) {

            User user = authService.getUser(username);

            HttpSession oldSession = req.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }

            HttpSession session = req.getSession(true);
            session.setAttribute("user", user);

            res.setContentType("application/json;charset=UTF-8");
            res.getWriter().write(
                    UserGsonFactory.getGson().toJson(user)
            );

        } else {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }
}


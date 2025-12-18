package controller;

import dao.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.*;
import java.io.IOException;

@WebServlet("/home")
public class HomeController extends HttpServlet {
    public HomeController(){}

    @Override
    public void init() throws ServletException {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        if(user.getRole() instanceof ProfessorRole)
            req.getRequestDispatcher(req.getContextPath() + "/homeProfessor").forward(req, resp);
        else if(user.getRole() instanceof StudentRole)
            req.getRequestDispatcher(req.getContextPath() + "/homeStudent").forward(req, resp);
        else
            req.getRequestDispatcher("/jsp/home.jsp")
                    .forward(req, resp);
    }
}

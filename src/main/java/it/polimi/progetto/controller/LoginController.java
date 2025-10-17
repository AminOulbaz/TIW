package it.polimi.progetto.controller;

import it.polimi.progetto.bean.UserBean;
import it.polimi.progetto.dao.UserDAO;
import it.polimi.progetto.util.Role;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.mariadb.jdbc.Connection;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;

@WebServlet(name = "LoginController", value = "/login")
public class LoginController extends HttpServlet {
    private TemplateEngine templateEngine;
    private JakartaServletWebApplication application;

    @Override
    public void init() throws ServletException {
        this.application = (JakartaServletWebApplication) getServletContext().getAttribute("jakartaWebApp");
        this.templateEngine = (TemplateEngine) getServletContext().getAttribute("thymeleafEngine");
        System.out.println("LoginController servlet init");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        WebContext ctx = new WebContext(application.buildExchange(req,resp));
        ctx.setLocale(req.getLocale());

        resp.setContentType("text/html;charset=UTF-8");
        templateEngine.process("login", ctx, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        for(String k : req.getParameterMap().keySet()){
            System.out.println(k + ": " + req.getParameter(k));
        }
        Role role = Role.valueOf(req.getParameter("role").toUpperCase());
        //TODO: cambiare valore della enum per uniformare i valori db e dao

        UserDAO userDAO = new UserDAO((Connection) getServletContext().getAttribute("dbConnection"));
        UserBean userBean = userDAO.getUser(username,password, role);

        if (userBean != null) {
            System.out.println("User logged in "+userBean);
            HttpSession session = req.getSession();
            session.setAttribute("user", userBean);
            req.getRequestDispatcher("/home").forward(req, resp);
            //resp.sendRedirect("/home");

        } else {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}

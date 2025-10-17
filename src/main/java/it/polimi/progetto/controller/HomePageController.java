package it.polimi.progetto.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;

@WebServlet(name = "HomePageController", value = "/home")
public class HomePageController extends HttpServlet {
    private TemplateEngine templateEngine;
    private JakartaServletWebApplication application;

    @Override
    public void init() throws ServletException {
        this.application = (JakartaServletWebApplication) getServletContext().getAttribute("jakartaWebApp");
        this.templateEngine = (TemplateEngine) getServletContext().getAttribute("thymeleafEngine");
        System.out.println("HomePageController servlet init");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        System.out.println("HomePageController doPost");

        WebContext ctx = new WebContext(application.buildExchange(req, resp));
        ctx.setLocale(req.getLocale());
        resp.setContentType("text/html;charset=UTF-8");
        templateEngine.process("home", ctx, resp.getWriter());
    }
}
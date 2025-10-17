package it.polimi.progetto.controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebListener;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

@WebListener
public class AppInitializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            ServletContext ctx = sce.getServletContext();

            //it is an unified view of the application
            //contains servlet, context, sessions, requests, responses and so on..
            JakartaServletWebApplication app = JakartaServletWebApplication.buildApplication(ctx);
            ctx.setAttribute("jakartaWebApp", app);

            // initialization of the thymeleaf engine once when the application bootstraps
            TemplateConfig.init(ctx);
            System.out.println("Servlet context initialized");
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // eventuale cleanup
    }
}


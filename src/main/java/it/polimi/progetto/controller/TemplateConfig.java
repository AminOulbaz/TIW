package it.polimi.progetto.controller;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

public class TemplateConfig {

    private static TemplateEngine thymeleafEngine;
    private static TemplateEngine freemarkerEngine;

    public static void init(ServletContext ctx) throws ServletException {
        JakartaServletWebApplication application = JakartaServletWebApplication.buildApplication(ctx);
        ctx.setAttribute("jakartaWebApp", application);

        WebApplicationTemplateResolver resolver =
                new WebApplicationTemplateResolver(application);


        resolver.setPrefix("/WEB-INF/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheTTLMs(3600000L);

        thymeleafEngine = new TemplateEngine();
        thymeleafEngine.setTemplateResolver(resolver);
        ctx.setAttribute("thymeleafEngine", thymeleafEngine);

        freemarkerEngine = new TemplateEngine();
        freemarkerEngine.setTemplateResolver(resolver);
        ctx.setAttribute("freemarkerEngine", freemarkerEngine);

    }

    public static TemplateEngine getThymeleafEngine() {
        return thymeleafEngine;
    }

    public static TemplateEngine getFreemarkerEngine() {
        return freemarkerEngine;
    }
}

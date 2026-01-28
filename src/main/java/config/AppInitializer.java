package config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebListener
public class AppInitializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        /*
        * BEGIN DATABASE CONNECTION CONFIGURATION:
        * get init values via web.xml with context-param
        * */
        ServletContext ctx = sce.getServletContext();
        String dbUrl = ctx.getInitParameter("db.url");
        String dbUser = ctx.getInitParameter("db.user");
        String dbPwd = ctx.getInitParameter("db.password");
        String dbDriver = ctx.getInitParameter("db.driver");
        try {
            Class.forName(dbDriver);
            Connection connection = DriverManager.getConnection(dbUrl,dbUser,dbPwd);
            ctx.setAttribute("dbConnection",connection);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("ServletContextListener initialized");
        /*
         * END DATABASE CONNECTION CONFIGURATION
         * */
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }
}

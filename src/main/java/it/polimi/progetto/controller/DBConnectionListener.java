package it.polimi.progetto.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class DBConnectionListener implements ServletContextListener {

    private Connection connection;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            // Crea la connessione
            String url = "jdbc:mariadb://localhost:3307/verbalizzazioneesami";
            String user = "root";
            String password = "root";
            connection = DriverManager.getConnection(url, user, password);

            // Salva la connessione nel ServletContext
            sce.getServletContext().setAttribute("dbConnection", connection);

            System.out.println("Connessione al database MariaDB inizializzata con successo!");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore durante l'inizializzazione della connessione DB", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("❎ Connessione al database chiusa.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

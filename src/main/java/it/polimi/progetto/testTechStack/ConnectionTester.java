package it.polimi.progetto.testTechStack;

import java.sql.*;
public class ConnectionTester {
    public static void main(String[] args) throws SQLException,
            ClassNotFoundException {
        final String DATABASE = "progetto";
        final String USER = "amin";
        final String PASSWORD = "nima";
        Connection connection = null;
        // Load the JDBC driver
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            System.out.println("Driver loaded");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver not found");
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection
                    ("jdbc:mariadb://localhost:3307/" + DATABASE, USER, PASSWORD);
            System.out.println("Database connected");
            connection.close();
        } catch (Exception e) {
            System.out.println("Connection failed");
            e.printStackTrace();
        }
    }
}

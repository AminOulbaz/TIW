package service;

import dao.*;
import jakarta.servlet.ServletContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ProfessorServiceFactory {
    private static ProfessorService professorService;

    public static ProfessorService getProfessorService(ServletContext ctx) throws SQLException {
        if (professorService == null) {
            Connection conn = DriverManager.getConnection(
                    ctx.getInitParameter("db.url"),
                    ctx.getInitParameter("db.user"),
                    ctx.getInitParameter("db.password")
            );
            professorService = new ProfessorService(
                    new ProfessorDaoImpl(conn),
                    new CourseDaoImpl(conn),
                    new ExamSessionDaoImpl(conn),
                    new ExamResultDaoImpl(conn),
                    new VerbalDaoImpl(conn)
            );
        }
        return professorService;
    }
}


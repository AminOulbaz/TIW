package service;

import dao.*;
import jakarta.servlet.ServletContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class StudentServiceFactory {
    private static StudentService studentService;

    public static StudentService getStudentService(ServletContext ctx) throws SQLException {
        if (studentService == null) {
            Connection connection = DriverManager.getConnection(
                    ctx.getInitParameter("db.url"),
                    ctx.getInitParameter("db.user"),
                    ctx.getInitParameter("db.password")
            );
            studentService = new StudentService(
                    new StudentDaoImpl(connection),
                    new CourseDaoImpl(connection),
                    new ExamSessionDaoImpl(connection),
                    new ExamResultDaoImpl(connection)
            );
        }
        return studentService;
    }
}


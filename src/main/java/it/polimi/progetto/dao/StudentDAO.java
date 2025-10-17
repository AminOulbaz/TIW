package it.polimi.progetto.dao;

import it.polimi.progetto.bean.*;
import it.polimi.progetto.util.Role;
import org.mariadb.jdbc.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentDAO {
    private Connection connection;
    public StudentDAO(Connection connection) {
        this.connection = connection;
    }
    public StudentBean getStudentByUser(UserBean user) throws SQLException {
        StudentBean studentBean = null;
        String queryForGettingPersonInformation =
                "SELECT matricola, nome, cognome, email,corsoDiLaurea" +
                " FROM studente WHERE username = ?";

        try (PreparedStatement ps = connection.prepareStatement(queryForGettingPersonInformation)) {
            ps.setString(1, user.getUsername());
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    DegreeDAO degreeDAO = new DegreeDAO(connection);
                    studentBean = new StudentBean(
                            rs.getString("matricola"),
                            rs.getString("nome"),
                            rs.getString("cognome"),
                            rs.getString("email"),
                            new DegreeBean(
                                    rs.getString("corsoDiLaurea"),
                                    degreeDAO.getNameByCode(rs.getString("corsoDiLaurea"))),
                            user);
                }
            }
        }
        return studentBean;
    }
}

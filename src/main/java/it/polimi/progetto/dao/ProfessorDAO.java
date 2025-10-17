package it.polimi.progetto.dao;

import it.polimi.progetto.bean.*;
import it.polimi.progetto.util.Role;
import org.mariadb.jdbc.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfessorDAO {
    private Connection connection;
    public ProfessorDAO(Connection connection) {
        this.connection = connection;
    }
    public ProfessorBean getProfessorByUser(UserBean user) throws SQLException {
        ProfessorBean professorBean = null;
        String queryForGettingPersonInformation =
                "SELECT matricola, nome, cognome, email" +
                        " FROM docente WHERE username = ?";

        try (PreparedStatement ps = connection.prepareStatement(queryForGettingPersonInformation)) {
            ps.setString(1, user.getUsername());
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    professorBean = new ProfessorBean(rs.getString("matricola"),
                            rs.getString("nome"),
                            rs.getString("cognome"),
                            rs.getString("email"),
                            user);
                }
            }
        }

        return professorBean;
    }
}

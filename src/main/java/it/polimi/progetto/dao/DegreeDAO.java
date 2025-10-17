package it.polimi.progetto.dao;

import it.polimi.progetto.bean.UserBean;
import it.polimi.progetto.util.PasswordUtils;
import it.polimi.progetto.util.Role;
import org.mariadb.jdbc.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DegreeDAO {
    private Connection connection;
    public DegreeDAO(Connection connection) {
        this.connection = connection;
    }

    public String getNameByCode(String code) {
        String name = null;
        String queryForGettingNameOfDegree =
                "SELECT nome FROM laurea WHERE codice = ?";

        try (PreparedStatement ps = connection.prepareStatement(queryForGettingNameOfDegree)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next())
                    name = rs.getString("nome");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return name;
    }
}

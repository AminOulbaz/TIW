package it.polimi.progetto.dao;

import it.polimi.progetto.bean.*;
import it.polimi.progetto.util.PasswordUtils;
import it.polimi.progetto.util.Role;
import org.mariadb.jdbc.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private Connection connection;
    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public UserBean getUser(String username, String plainPassword, Role role) {
        String queryForCheckingCredential = "SELECT password FROM credenziali WHERE username = ? and ruolo = ?";
        UserBean user = null;
        try (PreparedStatement ps = connection.prepareStatement(queryForCheckingCredential)) {
            ps.setString(1, username);
            ps.setString(2, role.getDescription());
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    String hashPassword = rs.getString("password");
                    if(PasswordUtils.checkPassword(plainPassword, hashPassword))
                       user = new UserBean(username,hashPassword,role);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }
}

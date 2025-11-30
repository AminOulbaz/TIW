package dao;

import model.StudentRole;
import model.ProfessorRole;
import model.User;

import java.sql.*;
import java.util.ArrayList;

public class UserDaoImpl implements UserDao{
    private Connection conn;

    public UserDaoImpl(Connection conn) {
        this.conn = conn;
    }

    private PreparedStatement prepareSQLstatement(String sql, ArrayList<Object> parameters){
        PreparedStatement stmt = null;
        try{
            stmt = conn.prepareStatement(sql);
            for(int i = 0; i < parameters.size(); i++){
                stmt.setObject(i + 1, parameters.get(i));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return stmt;
    }

    public User findByEmailAndPassword(String email, String password) {
        try {
            PreparedStatement stmt = conn.prepareStatement("""
                SELECT username, email, role
                FROM users
                WHERE email = ? AND password = ?
            """);
            stmt.setString(1, email);
            stmt.setString(2, password);


            ResultSet rs = stmt.executeQuery();
//
//            ResultSet rs = prepareSQLstatement("""
//                SELECT username, email, role
//                FROM users
//                WHERE email = ? AND password = ?
//            """ , ).executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("role").equals("STUDENT") ? new StudentRole() :
                                rs.getString("role").equals("TEACHER") ? new ProfessorRole() : null
                );
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User findByUsername(String username) {
        String sql = "SELECT username, password FROM users WHERE username = ?";

        try{
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getString("username"),
                        rs.getString("password")
                );
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Errore durante l'accesso al DB", e);
        }
    }

    /**
     * @param username
     * @param password
     * @return
     */
    @Override
    public User findByUsernameAndPassword(String username, String password) {
        return null;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public User findById(int id) {
        return null;
    }

    @Override
    public User findByEmail(String email) {
        return null;
    }
}

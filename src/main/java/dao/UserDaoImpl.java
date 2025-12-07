package dao;

import model.StudentRole;
import model.ProfessorRole;
import model.User;

import javax.management.relation.Role;
import java.sql.*;
import java.util.ArrayList;

public class UserDaoImpl implements UserDao{
    private Connection connection;

    public UserDaoImpl(Connection connection) {
        this.connection = connection;
    }
    /**
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {
        try {
            PreparedStatement stmt = connection.prepareStatement("""
                SELECT password, email, role
                FROM users
                WHERE username = ?
            """);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User(
                        username,
                        rs.getString("password")
                );
                user.setEmail(rs.getString("email"));
                user.setRole(
                        rs.getString("role").equals("STUDENT") ?
                                new StudentRole() :
                                rs.getString("role").equals("TEACHER") ?
                                        new ProfessorRole() : null
                );
                return user;
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void registerUser(String paramUsername, String paramPassword, String paramEmail, String paramRole) {
        try{
            PreparedStatement psmt = connection.prepareStatement("""
            insert into users(username, password, email, role) values 
           (?,?,?,?)
""");
            psmt.setString(1,paramUsername);
            psmt.setString(2,paramPassword);
            psmt.setString(3,paramEmail);
            psmt.setString(4,paramRole);
            psmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private PreparedStatement createUpdateStatement(String field) throws SQLException {
        String query = "update users \nset ";
        String selection = field.substring(0,1);
        if(selection.equals("p"))
            query += "password = ?\n";
        else if(selection.equals("e"))
            query += "email = ?\n";
        else if(selection.equals("r"))
            query += "role = ?\n";
        query+= "where username = ?;";
        return connection.prepareStatement(query);
    }

    @Override
    public void updatePassword(String username, String password) {
        try{
            PreparedStatement psmt = createUpdateStatement("p"+password);
            psmt.setString(1 , password);
            psmt.setString(2, username);
            psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateEmail(String username, String email) {
        try{
            PreparedStatement psmt = createUpdateStatement("e"+email);
            psmt.setString(1 , email);
            psmt.setString(2, username);
            psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateRole(String username, String role) {
        try{
            PreparedStatement psmt = createUpdateStatement("r"+role);
            psmt.setString(1 , role);
            psmt.setString(2, username);
            psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUser(String username) {
        try{
            PreparedStatement psmt = connection.prepareStatement("""
            delete from users where username = ?;
""");
            psmt.setString(1,username);
            psmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

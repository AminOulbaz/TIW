package dao;

import model.User;

public interface UserDao {
    User findByUsername(String username);
    void registerUser(String username, String password, String email, String role);
    void updatePassword(String username, String password);
    void updateEmail(String username, String email);
    void updateRole(String username, String role);
    void deleteUser(String username);
}


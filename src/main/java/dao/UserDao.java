package dao;

import model.User;

public interface UserDao {
    User findByUsername(String username);
    User findByUsernameAndPassword(String username, String password);
    User findById(int id);
    User findByEmail(String email);
}


package service;

import model.User;
import dao.UserDao;

public class AuthService {

    private final UserDao dao;

    public AuthService(UserDao dao) {
        this.dao = dao;
    }

    public boolean login(String username, String password) {
        User user = dao.findByUsername(username);
        if (user == null) return false;
        return user.checkPassword(password);
    }
}


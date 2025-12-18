package service;

import exception.validation.NoUserFoundException;
import exception.validation.PasswordIncorrectException;
import model.User;
import dao.UserDao;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class AuthService {

    private final UserDao dao;
    private BCryptPasswordEncoder encoder;

    public AuthService(UserDao dao) {
        this.dao = dao;
        encoder = new BCryptPasswordEncoder();
    }

    public User getUser(String username){
        return dao.findByUsername(username);
    }

    public void register(String username, String password,
                         String email, String role){
        String hashed = encoder.encode(password);
        System.out.println(hashed);
        dao.registerUser(username, hashed, email, role);
    }

    public void updatePassword(String username, String password){
        String hashed = encoder.encode(password);
        dao.updatePassword(username,hashed);
    }

    public boolean login(String username, String password) {
        User user = getUser(username);
        if (user == null) throw new NoUserFoundException("no user found with this username "+username);
        return encoder.matches(password, user.getPassword());
    }

    public void deleteUser(String username){
        if (getUser(username) == null) throw new NoUserFoundException("no user found with this username "+username);
        dao.deleteUser(username);
    }

    public String getHomePage(String username){
        User user = getUser(username);
        if (user == null) throw new NoUserFoundException("no user found with this username "+username);
        return user.getHomePage();
    }
}


package model;

public class User {
    private String username;
    private String password;
    private String email;
    private Role role;


    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, Role role) {
        this(username, password);
        this.role = role;
    }

    public boolean checkPassword(String password) {
        return hash(this.password).equals(hash(password));
    }

    private String hash(String s) {
        return Integer.toHexString(s.hashCode());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    public String getHomePage() { return role.homePage();}

    @Override
    public String toString() {
        return "User{" +
               "username='" + username + '\'' +
               ", password='" + password + '\'' +
               ", email='" + email + '\'' +
               ", role=" + role +
               '}';
    }
}


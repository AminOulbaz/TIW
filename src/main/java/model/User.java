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
        //TODO: hashing
        return Integer.toHexString(s.hashCode());
    }

    public void setEmail(String email) {this.email = email;}
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getHomePage() { return role.homePage();}
    public Role getRole() { return role; }
}


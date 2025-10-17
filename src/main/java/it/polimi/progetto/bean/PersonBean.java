package it.polimi.progetto.bean;

public class PersonBean {
    private String name;
    private String surname;
    private String email;
    private String identifier;

    PersonBean(String name, String surname, String email, String identifier) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.identifier = identifier;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}

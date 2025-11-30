package model;

public class Student {
    private String id;
    private String name;
    private String surname;
    private String email;
    private int degreeProgramId;

    public Student() {}

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public int getDegreeProgramId() {
        return degreeProgramId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDegreeProgramId(int degreeProgramId) {
        this.degreeProgramId = degreeProgramId;
    }
}

package model;

public class Student {
    private String id;
    private String name;
    private String surname;
    private String email;
    private int yearOfStudy;
    private String degreeProgramCode;

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

    public String getDegreeProgramCode() {
        return degreeProgramCode;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
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

    public void setDegreeProgramCode(String degreeProgramCode) {
        this.degreeProgramCode = degreeProgramCode;
    }

    @Override
    public String toString() {
        return "Student{" +
               "id='" + id + '\'' +
               ", name='" + name + '\'' +
               ", surname='" + surname + '\'' +
               ", email='" + email + '\'' +
               ", yearOfStudy=" + yearOfStudy +
               ", degreeProgramCode='" + degreeProgramCode + '\'' +
               '}';
    }
}

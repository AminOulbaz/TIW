package it.polimi.progetto.bean;

import java.util.HashSet;
import java.util.Set;

public class ProfessorBean extends PersonBean {
    private UserBean user;
    private Set<CourseBean> courses;
    private Set<VerbalBean> verbals;
    public ProfessorBean(String name,
                  String surname,
                  String email,
                  String identifier, UserBean user) {
        super(name, surname, email, identifier);
        this.user = user;
        this.courses = new HashSet<CourseBean>();
        this.verbals = new HashSet<VerbalBean>();
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public Set<CourseBean> getCourses() {
        return new HashSet<>(courses);
    }

    public void setCourses(Set<CourseBean> courses) {
        this.courses = courses;
    }

    public Set<VerbalBean> getVerbals() {
        return new HashSet<>(verbals);
    }

    public void setVerbals(Set<VerbalBean> verbals) {
        this.verbals = verbals;
    }
}

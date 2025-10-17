package it.polimi.progetto.bean;

public class StudentBean extends PersonBean{
    private DegreeBean degree;
    private UserBean user;

    public StudentBean(String name,
                String surname,
                String email,
                String identifier,
                DegreeBean degree,
                UserBean user) {
        super(name, surname, email, identifier);
        this.degree = degree;
        this.user = user;
    }

    public DegreeBean getDegree() {
        return degree;
    }

    public void setDegree(DegreeBean degree) {
        this.degree = degree;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }
}

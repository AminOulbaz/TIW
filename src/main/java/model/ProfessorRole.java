package model;

public class ProfessorRole implements Role{
    public String homePage(){
        return "/professor/home";
    }

    @Override
    public String toString() {
        return "PROFESSOR";
    }
}

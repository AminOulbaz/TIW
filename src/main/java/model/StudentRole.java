package model;

public class StudentRole implements Role{
    public String homePage(){ return "/student/home"; }

    @Override
    public String toString() { return "STUDENT"; }
}

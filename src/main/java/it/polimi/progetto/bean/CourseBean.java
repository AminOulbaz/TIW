package it.polimi.progetto.bean;

public class CourseBean {
    private String name;
    private String code;
    private long year,semester;
    private double cfu;
    private DegreeBean degree;

    CourseBean(String name, String code, long year, long semester, double cfu, DegreeBean degree) {
        this.name = name;
        this.code = code;
        this.year = year;
        this.semester = semester;
        this.cfu = cfu;
        this.degree = degree;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getYear() {
        return year;
    }

    public void setYear(long year) {
        this.year = year;
    }

    public long getSemester() {
        return semester;
    }

    public void setSemester(long semester) {
        this.semester = semester;
    }

    public double getCfu() {
        return cfu;
    }

    public void setCfu(double cfu) {
        this.cfu = cfu;
    }

    public DegreeBean getDegree() {
        return degree;
    }

    public void setDegree(DegreeBean degree) {
        this.degree = degree;
    }
}

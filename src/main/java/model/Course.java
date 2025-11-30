package model;

public class Course {
    private String code;
    private String name;
    private float credits;
    private int degreeProgramId;

    public Course() {}
    public Course(String code, String name, float credits, int degreeProgramId) {
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.degreeProgramId = degreeProgramId;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public float getCredits() {
        return credits;
    }

    public int getDegreeProgramId() {
        return degreeProgramId;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCredits(float credits) {
        this.credits = credits;
    }

    public void setDegreeProgramId(int degreeProgramId) {
        this.degreeProgramId = degreeProgramId;
    }
}

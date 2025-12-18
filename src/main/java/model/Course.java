package model;

public class Course {
    private String code;
    private String name;
    private float credits;
    private String degreeProgramCode;
    private String professorId;

    public Course() {}
    public Course(String code, String name, float credits, String degreeProgramId) {
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.degreeProgramCode = degreeProgramCode;
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

    public String getDegreeProgramId() {
        return degreeProgramCode;
    }

    public String getDegreeProgramCode() {
        return degreeProgramCode;
    }

    public String getProfessorId() {
        return professorId;
    }

    public void setProfessorId(String professorId) {
        this.professorId = professorId;
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

    public void setDegreeProgramCode(String degreeProgramCode) {
        this.degreeProgramCode = degreeProgramCode;
    }

    @Override
    public String toString() {
        return "Course{" +
               "code='" + code + '\'' +
               ", name='" + name + '\'' +
               ", credits=" + credits +
               ", degreeProgramCode='" + degreeProgramCode + '\'' +
               ", professorId='" + professorId + '\'' +
               '}';
    }
}

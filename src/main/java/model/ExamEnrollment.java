package model;

import java.util.Date;

public class ExamEnrollment {
    private int examSessionId;
    private String studentId;
    private Date enrollmentDate;

    public ExamEnrollment(){}
    public ExamEnrollment(int examSessionId, String studentId) {
        this.examSessionId = examSessionId;
        this.studentId = studentId;
    }

    public int getExamSessionId() {
        return examSessionId;
    }

    public String getStudentId() {
        return studentId;
    }

    public Date getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setExamSessionId(int examSessionId) {
        this.examSessionId = examSessionId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setEnrollmentDate(Date enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
}

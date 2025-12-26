package dto;

import model.ExamGrade;
import model.ExamStatus;

public class ExamResult {
    private String studentId;
    private int examId;
    private String status;
    private String grade;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
    @Override
    public String toString() {
        return "ExamResult{" +
                "studentId='" + studentId + '\'' +
                ", examId=" + examId +
                ", status=" + status +
                ", grade=" + grade +
                '}';
    }
}


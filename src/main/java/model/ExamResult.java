package model;

public class ExamResult {
    private String studentId;
    private int examId;
    private ExamStatus status;
    private ExamGrade grade;

    public ExamResult() {}

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

    public ExamStatus getStatus() {
        return status;
    }

    public void setStatus(ExamStatus status) {
        this.status = status;
    }

    public ExamGrade getGrade() {
        return grade;
    }

    public void setGrade(ExamGrade grade) {
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

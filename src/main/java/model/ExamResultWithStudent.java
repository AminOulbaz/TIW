package model;

public class ExamResultWithStudent{
    Student student;
    ExamResult examResult;
    public ExamResultWithStudent(Student student, ExamResult examResult) {
        this.student = student;
        this.examResult = examResult;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public ExamResult getExamResult() {
        return examResult;
    }

    public void setExamResult(ExamResult examResult) {
        this.examResult = examResult;
    }
}

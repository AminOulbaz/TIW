package model;

public class ExamResult{
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
    protected ExamResult clone(){
        ExamResult examResult = new ExamResult();
        examResult.setExamId(this.examId);
        examResult.setStudentId(this.studentId);
        examResult.setStatus(this.status);
        examResult.setGrade(this.grade);
        return examResult;
    }

    public ExamResult getDifferentExamResultWithDifferentStatus(ExamStatus status){
        ExamResult examResult = clone();
        examResult.setStatus(status);
        return examResult;
    }
    public ExamResult getDifferentExamResultWithDifferentGrade(ExamGrade grade){
        ExamResult examResult = clone();
        examResult.setGrade(grade);
        return examResult;
    }
    public ExamResult getExamResultWithDifferentStatus(ExamStatus status){
        setStatus(status);
        return this;
    }
    public ExamResult getExamResultWithDifferentGrade(ExamGrade grade){
        setGrade(grade);
        return this;
    }

    @Override
    public String toString() {
        return "ExamResultDto{" +
                "studentId='" + studentId + '\'' +
                ", examId=" + examId +
                ", status=" + status +
                ", grade=" + grade +
                '}';
    }
}

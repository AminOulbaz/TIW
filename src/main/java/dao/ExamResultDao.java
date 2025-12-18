package dao;

import model.ExamResult;
import model.ExamResultWithStudent;
import model.Student;

import java.util.List;

public interface ExamResultDao {
    public void updateExamResult(ExamResult examResult);
    public ExamResult getExamResultByExamSessionIdAndStudentId(int examSessionId, String studentId);
    public List<ExamResult> getExamResultsByExamSessionId(int examSessionId);
    public List<ExamResult> getExamResultsByStudentId(String studentId);
    public void insertExamResult(ExamResult examResult);
    public void deleteExamResult(int examSessionId, String studentId);
    List<Student> getStudentsByExamSessionId(int examSessionId);
    List<ExamResultWithStudent> getExamResultWithStudentsByExamSessionId(int examSessionId);
    List<ExamResultWithStudent> getExamResultWithStudentsByExamSessionId(int examSessionId, String sortKey, String sortOrder);
    ExamResultWithStudent getExamResultWithStudentByExamSessionIdAndStudentId(int examSessionId, String studentId);
}

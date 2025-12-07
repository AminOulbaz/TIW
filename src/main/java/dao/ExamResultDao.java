package dao;

import model.ExamResult;

import java.util.List;

public interface ExamResultDao {
    public void updateExamResult(ExamResult examResult);
    public ExamResult getExamResultByExamSessionIdAndStudentId(int examSessionId, String studentId);
    public List<ExamResult> getExamResultsByExamSessionId(int examSessionId);
}

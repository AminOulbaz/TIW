package dao;

import model.ExamResult;

public interface ExamResultDao {
    public void updateExamResult(ExamResult examResult);
    public ExamResult getExamResultByExamSessionIdAndStudentId(int examSessionId, String studentId);

}

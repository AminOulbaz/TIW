package dao;

import model.ExamSession;

import java.util.List;

public interface ExamSessionDao {
    public List<ExamSession> getExamSessionsByCourse(String courseId);
}

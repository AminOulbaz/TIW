package dao;

import model.ExamSession;
import model.Student;

import java.util.List;

public interface ExamSessionDao {
    public List<ExamSession> getExamSessionsByCourse(String courseId);
    public ExamSession getExamSessionById(int examSessionId);
}

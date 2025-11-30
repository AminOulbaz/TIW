package dao;

import model.ExamEnrollment;

import java.util.List;

public interface ExamEnrollmentStudentDao {
    public List<ExamEnrollment> getEnrolledStudentsByExamSession(int examSessionId);
}

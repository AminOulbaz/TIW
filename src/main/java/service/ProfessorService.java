package service;

import dao.*;
import model.Course;
import model.ExamEnrollment;
import model.ExamResult;
import model.ExamSession;

import java.sql.SQLException;
import java.util.List;

public class ProfessorService {

    private ProfessorDao professorDao;
    private CourseDao courseDao;
    private ExamSessionDao examSessionDao;
    private ExamEnrollmentStudentDao examEnrollmentStudentDao;
    private ExamResultDao examResultDao;

    public ProfessorService(ProfessorDao professorDao,
                            CourseDao courseDao,
                            ExamSessionDao examSessionDao,
                            ExamEnrollmentStudentDao examEnrollmentStudentDao,
                            ExamResultDao examResultDao) {
        this.professorDao = professorDao;
        this.courseDao = courseDao;
        this.examSessionDao = examSessionDao;
        this.examEnrollmentStudentDao = examEnrollmentStudentDao;
        this.examResultDao = examResultDao;
    }

    public List<Course> getCoursesByProfessor(String professorId){
        return courseDao.getCoursesByProfessorId(professorId);
    }
    public List<ExamSession> getExamSessionsByCourse(String courseId){
        return examSessionDao.getExamSessionsByCourse(courseId);
    }
    public List<ExamEnrollment> getEnrolledStudentByExamSession(int examSessionId){
        return examEnrollmentStudentDao.getEnrolledStudentsByExamSession(examSessionId);
    }
    public ExamResult getExamResultByExamSessionIdAndStudentId(int examSessionId,String studentId){
        return examResultDao.getExamResultByExamSessionIdAndStudentId(examSessionId,studentId);
    }
    public void updateExamResult(ExamResult examResult){
        examResultDao.updateExamResult(examResult);
//        try {
//            examResultDao.updateExamResult(examResult);
//        } catch (SQLException e) {
//            throw new ExamUpdateFailedException("Database error while updating result", e);
//        } catch (InvalidGradeException | StudentNotEnrolledException e) {
//            throw e; // errori di dominio che il controller deve gestire
//        }
    }
}

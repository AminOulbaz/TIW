package service;

import dao.*;
import model.*;

import java.util.List;

public class ProfessorService {

    private ProfessorDao professorDao;
    private CourseDao courseDao;
    private ExamSessionDao examSessionDao;
    private ExamEnrollmentStudentDao examEnrollmentStudentDao;
    private ExamResultDao examResultDao;
    private VerbalDao verbalDao;

    public ProfessorService(ProfessorDao professorDao,
                            CourseDao courseDao,
                            ExamSessionDao examSessionDao,
                            ExamEnrollmentStudentDao examEnrollmentStudentDao,
                            ExamResultDao examResultDao,
                            VerbalDao verbalDao) {
        this.professorDao = professorDao;
        this.courseDao = courseDao;
        this.examSessionDao = examSessionDao;
        this.examEnrollmentStudentDao = examEnrollmentStudentDao;
        this.examResultDao = examResultDao;
        this.verbalDao = verbalDao;
    }
    public Professor getProfessorByUsername(String professorUsername) {
        return professorDao.getProfessorByUserId(professorUsername);
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
    public List<ExamResult> getExamResultsByExamSessionId(int examSessionId){
        return examResultDao.getExamResultsByExamSessionId(examSessionId);
    }
    public void updateExamResult(ExamResult examResult){
        examResultDao.updateExamResult(examResult);
    }
    public void createVerbal(Verbal verbal){
        verbalDao.createVerbal(verbal);
    }
    public Verbal getVerbalById(int verbalId){
        return verbalDao.getVerbal(verbalId);
    }
}

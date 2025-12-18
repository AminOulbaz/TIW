package service;

import dao.*;
import model.*;

import java.sql.Timestamp;
import java.util.List;

public class ProfessorService {

    private ProfessorDao professorDao;
    private CourseDao courseDao;
    private ExamSessionDao examSessionDao;
    private ExamResultDao examResultDao;
    private VerbalDao verbalDao;

    public ProfessorService(ProfessorDao professorDao,
                            CourseDao courseDao,
                            ExamSessionDao examSessionDao,
                            ExamResultDao examResultDao,
                            VerbalDao verbalDao) {
        this.professorDao = professorDao;
        this.courseDao = courseDao;
        this.examSessionDao = examSessionDao;
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
    public Verbal getVerbalById(String verbalId){
        return verbalDao.findVerbal(verbalId);
    }
    public List<Verbal> getAllVerbals(String professorId){
        return verbalDao.findVerbalsByProfessorId(professorId);
    }
    public List<Verbal> getAllVerbalsOrdered(String professorId){
        return verbalDao.findVerbalsByProfessorIdOrdered(professorId);
    }
    public List<Verbal> getAllVerbalsByExamSessionId(int examSessionId){
        return verbalDao.findAllVerbalsByExamSessionId(examSessionId);
    }
    public void updateVerbal(Timestamp creationTimestamp, String professorId, int id) {
        verbalDao.updateVerbalByProfessorIdAndExamSessionId(creationTimestamp,professorId,id);
    }

    public ExamResultWithStudent getExamResultWithStudent(int examSessionId, String studentId){
        return examResultDao.getExamResultWithStudentByExamSessionIdAndStudentId(examSessionId,studentId);
    }

    public List<ExamResultWithStudent> getExamResultWithStudentsByExamSessionId(int examSessionId) {
        return examResultDao.getExamResultWithStudentsByExamSessionId(examSessionId);
    }
    public List<ExamResultWithStudent> getExamResultWithStudentsByExamSessionId(int examSessionId, String sortKey, String ordKey) {
        return examResultDao.getExamResultWithStudentsByExamSessionId(examSessionId, sortKey, ordKey);
    }

    public ExamSession getExamSessionByExamSessionId(int examSessionId){
        return examSessionDao.getExamSessionById(examSessionId);
    }
}

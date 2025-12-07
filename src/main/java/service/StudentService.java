package service;

import dao.*;
import exception.validation.StudentFailAuthenticationException;
import model.*;

import java.util.List;

public class StudentService {
    private StudentDao studentDao;
    private CourseDao courseDao;
    private ExamSessionDao examSessionDao;
    private ExamEnrollmentStudentDao examEnrollmentStudentDao;
    private ExamResultDao examResultDao;

    public StudentService(StudentDao studentDao, CourseDao courseDao, ExamSessionDao examSessionDao, ExamResultDao examResultDao) {
        this.studentDao = studentDao;
        this.courseDao = courseDao;
        this.examSessionDao = examSessionDao;
        this.examResultDao = examResultDao;
    }

    /*
        Grounding methods to satisfy business logic of the webapp:
         */
    public Student getStudentByUsername(String studentUsername){
        Student student = studentDao.getStudentByUserId(studentUsername);
        if(student == null){
            throw new StudentFailAuthenticationException("does not exist a student with this username "+studentUsername);
        }
        return student;
    }
    public List<Course> getCoursesByDegreeCode(String degreeCode){
        return courseDao.getCourseByDegreeCode(degreeCode);
    }
    public List<ExamSession> getExamSessionByCourseId(String courseId){
        return examSessionDao.getExamSessionsByCourse(courseId);
    }
    public ExamResult getExamResultByExamSessionAndStudentId(int examSessionId,String studentId){
        return examResultDao.getExamResultByExamSessionIdAndStudentId(examSessionId,studentId);
    }
    public String getExamResultNotificationByExamSessionAndStudentId(int examSessionId,String studentId){
        return getExamResultByExamSessionAndStudentId(examSessionId,studentId).getStatus().equals(ExamStatus.NOTINSERTED) ?
                "Voto non ancora definito" : null;
    }
    public void refuseExamResult(ExamResult examResult){
        examResultDao.updateExamResult(examResult.getExamResultWithDifferentStatus(ExamStatus.REFUSED));
    }

    /*
    Support method to simplify the control:
     */
    public List<Student> getStudents(){
        return studentDao.getStudents();
    }
    public boolean registerStudentToAppeal(int studentId, int appealId){return false;}
}

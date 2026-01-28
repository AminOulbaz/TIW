package service;

import dao.*;
import dto.ExamResultDto;
import model.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public Verbal makeVerbal(Professor professor, int examSessionId){
        Verbal verbal = new Verbal();
        verbal.setExamSession(getExamSessionByExamSessionId(examSessionId));
        verbal.setExamResultWithStudents(
                getExamResultWithStudentsByExamSessionId(examSessionId).stream().filter(
                        e -> e.getExamResult().getStatus().equals(ExamStatus.VERBALIZED) ||
                                e.getExamResult().getStatus().equals(ExamStatus.REFUSED)
                ).toList()
        );
        verbal.setCreationTimestamp(
                Timestamp.valueOf(LocalDateTime.now())
        );
        verbal.setProfessorId(professor.getId());
        verbal.setExamVerbalId(
                Verbal.generateCode(
                        verbal.getExamSession().getId(),
                        verbal.getProfessorId(),
                        verbal.getCreationTimestamp()
                )
        );
        return verbal;
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
    public List<ExamResultDto> getExamResultDtoWithStudentsByExamSessionId(int examSessionId) {
        List<ExamResultWithStudent> tmpResult = getExamResultWithStudentsByExamSessionId(examSessionId);
        List<ExamResultDto> dtoList = new ArrayList<ExamResultDto>();
        for (ExamResultWithStudent examResultWithStudent : tmpResult) {
            ExamResultDto examResultDto = new ExamResultDto();
            ExamResult examResult = examResultWithStudent.getExamResult();
            Student student = examResultWithStudent.getStudent();
            examResultDto.setExamId(examResult.getExamId());
            examResultDto.setStatus(examResult.getStatus().getLabel());
            examResultDto.setStudentId(student.getId());
            examResultDto.setGrade(examResult.getGrade().getLabel());
            examResultDto.setName(student.getName());
            examResultDto.setSurname(student.getSurname());
            examResultDto.setDegreeProgramCode(student.getDegreeProgramCode());
            examResultDto.setEmail(student.getEmail());
            dtoList.add(examResultDto);
        }
        return dtoList;
    }

    public ExamSession getExamSessionByExamSessionId(int examSessionId){
        return examSessionDao.getExamSessionById(examSessionId);
    }
}

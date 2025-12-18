package model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Verbal {
    private String examVerbalId;
    private ExamSession examSession;
    private String professorId;
    private List<ExamResultWithStudent> examResultWithStudents;
    private Timestamp creationTimestamp;

    public Verbal() {}

    public ExamSession getExamSession() {
        return examSession;
    }

    public void setExamSession(ExamSession examSession) {
        this.examSession = examSession;
    }

    public List<ExamResultWithStudent> getExamResultWithStudents() {
        return examResultWithStudents;
    }

    public void setExamResultWithStudents(List<ExamResultWithStudent> examResultWithStudents) {
        this.examResultWithStudents = examResultWithStudents;
    }

    public String getExamVerbalId() {
        return examVerbalId;
    }

    public String getProfessorId() {
        return professorId;
    }
    public Timestamp getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setExamVerbalId(String examVerbalId) {
        this.examVerbalId = examVerbalId;
    }

    public void setProfessorId(String professorId) {
        this.professorId = professorId;
    }

    public void setCreationTimestamp(Timestamp creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public static String generateCode(
            int examSessionId, String professorId, Timestamp creationTimestamp
    ){
        return Integer.toHexString(
                professorId.hashCode() + examSessionId + creationTimestamp.hashCode());
    }

    @Override
    public String toString() {
        return "Verbal{" +
                "examVerbalId='" + examVerbalId + '\'' +
                ", examSession=" + examSession +
                ", professorId='" + professorId + '\'' +
                ", examResultWithStudents=" + examResultWithStudents +
                ", creationTimestamp=" + creationTimestamp +
                '}';
    }
}

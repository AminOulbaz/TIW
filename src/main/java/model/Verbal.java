package model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Verbal {
    private int examVerbalId;
    private String professorId;
    private int examSessionId;
    private List<Student> students;
    private Timestamp creationTimestamp;

    public Verbal() {}

    public int getExamVerbalId() {
        return examVerbalId;
    }

    public String getProfessorId() {
        return professorId;
    }

    public int getExamSessionId() {
        return examSessionId;
    }

    public Timestamp getCreationTimestamp() {
        return creationTimestamp;
    }

    public List<Student> getStudents() {
        return new ArrayList<>(students);
    }

    public void setExamVerbalId(int examVerbalId) {
        this.examVerbalId = examVerbalId;
    }

    public void setProfessorId(String professorId) {
        this.professorId = professorId;
    }

    public void setExamSessionId(int examSessionId) {
        this.examSessionId = examSessionId;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public void setCreationTimestamp(Timestamp creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    @Override
    public String toString() {
        return "Verbal{" +
                "examVerbalId=" + examVerbalId +
                ", examSessionId=" + examSessionId +
                ", students=" + students +
                ", creationTimestamp=" + creationTimestamp +
                '}';
    }
}

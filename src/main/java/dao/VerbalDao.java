package dao;

import model.Verbal;

import java.sql.Timestamp;
import java.util.List;

public interface VerbalDao {
    public void createVerbal(Verbal verbal);
    public Verbal findVerbal(String verbalId);
    public List<Verbal> findVerbalsByProfessorId(String professorId);
    public List<Verbal> findAllVerbalsByExamSessionId(int examSessionId);
    public List<Verbal> findVerbalsByProfessorIdOrdered(String professorId);
    public void updateVerbalByProfessorIdAndExamSessionId(Timestamp verbalTimestamp, String professorId, int examSessionId);
}

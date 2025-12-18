package dao;

import model.ExamSession;
import model.Verbal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VerbalDaoImpl implements VerbalDao {
    private Connection connection;
    public VerbalDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createVerbal(Verbal verbal) {
        try{
            PreparedStatement psmt = connection.prepareStatement(
                    """
            INSERT INTO exam_verbal
                (exam_verbal_id, exam_session_id,creation_timestamp, professor_id) VALUES
            (?, ?, ?, ?);
""");
            psmt.setString(1, verbal.getExamVerbalId());
            psmt.setInt(2, verbal.getExamSession().getId());
            psmt.setTimestamp(3, verbal.getCreationTimestamp());
            psmt.setString(4, verbal.getProfessorId());
            psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Verbal findVerbal(String verbalId) {
        try{
            PreparedStatement psmt = connection.prepareStatement("""
        select exam_session_id, creation_timestamp, professor_id 
        from exam_verbal 
        where exam_verbal_id = ?
""");
            psmt.setString(1, verbalId);
            ResultSet rs = psmt.executeQuery();
            Verbal verbal = null;
            if(rs.next()) {
                verbal = new Verbal();
                ExamSession examSession = new ExamSession();
                examSession.setId(rs.getInt("exam_session_id"));
                verbal.setExamSession(examSession);
                verbal.setCreationTimestamp(rs.getTimestamp("creation_timestamp"));
                verbal.setProfessorId(rs.getString("professor_id"));
                verbal.setExamVerbalId(verbalId);
            }
            return verbal;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Verbal> processfindVerbalsByProfessorIdPreparedStatement(PreparedStatement psmt) throws SQLException {
        ResultSet rs = psmt.executeQuery();
        List<Verbal> verbals = new ArrayList<>();
        while(rs.next()) {
            Verbal verbal = new Verbal();
            ExamSession examSession = new ExamSession();
            examSession.setId(rs.getInt("exam_session_id"));
            verbal.setExamSession(examSession);
            verbal.setCreationTimestamp(rs.getTimestamp("creation_timestamp"));
            verbal.setProfessorId(rs.getString("professor_id"));
            verbal.setExamVerbalId(rs.getString("exam_verbal_id"));
            verbals.add(verbal);
        }
        return verbals;
    }

    @Override
    public List<Verbal> findVerbalsByProfessorId(String professorId) {
        try{
            PreparedStatement psmt = connection.prepareStatement("""
        select exam_verbal_id, exam_session_id, creation_timestamp, professor_id 
        from exam_verbal 
        where professor_id = ?
""");
            psmt.setString(1, professorId);
            return processfindVerbalsByProfessorIdPreparedStatement(psmt);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Verbal> findVerbalsByProfessorIdOrdered(String professorId) {
        try{
            PreparedStatement psmt = connection.prepareStatement("""
        select exam_verbal_id, exam_verbal.exam_session_id as exam_session_id, creation_timestamp, course_id, professor_id
        from exam_verbal join exam_session on exam_verbal.exam_session_id = exam_session.exam_session_id
        where professor_id = ?
        order by course_id, creation_timestamp
""");
            psmt.setString(1, professorId);
            return processfindVerbalsByProfessorIdPreparedStatement(psmt);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Verbal> findAllVerbalsByExamSessionId(int examSessionId) {
        try{
            PreparedStatement psmt = connection.prepareStatement("""
        select exam_verbal_id,professor_id, creation_timestamp 
        from exam_verbal 
        where exam_session_id = ?
""");
            psmt.setInt(1, examSessionId);
            ResultSet rs = psmt.executeQuery();
            List<Verbal> verbals = new ArrayList<>();
            while(rs.next()) {
                Verbal verbal = new Verbal();
                ExamSession examSession = new ExamSession();
                examSession.setId(examSessionId);
                verbal.setExamSession(examSession);
                verbal.setCreationTimestamp(rs.getTimestamp("creation_timestamp"));
                verbal.setProfessorId(rs.getString("professor_id"));
                verbal.setExamVerbalId(rs.getString("exam_verbal_id"));
                verbals.add(verbal);
            }
            return verbals;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateVerbalByProfessorIdAndExamSessionId(Timestamp verbalTimestamp, String professorId, int examSessionId) {
        try{
            PreparedStatement psmt = connection.prepareStatement("""
        select exam_verbal_id 
        from exam_verbal 
        where professor_id = ? and exam_session_id = ?
""");
            psmt.setString(1, professorId);
            psmt.setInt(2, examSessionId);
            ResultSet rs = psmt.executeQuery();
            if (rs.next()) {
                psmt = connection.prepareStatement(
                        """
                update exam_verbal
                set creation_timestamp = ?  
                where exam_verbal_id = ?
    """
                );
                psmt.setTimestamp(1, verbalTimestamp);
                psmt.setString(2, rs.getString("exam_verbal_id"));
                psmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

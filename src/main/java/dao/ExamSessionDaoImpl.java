package dao;

import model.ExamSession;
import model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExamSessionDaoImpl implements ExamSessionDao {
    private Connection connection;

    public ExamSessionDaoImpl(Connection connection) {
        this.connection = connection;
    }

    /**
     * @param courseId
     * @return
     */
    @Override
    public List<ExamSession> getExamSessionsByCourse(String courseId) {
        try{
            PreparedStatement psmt = connection.prepareStatement("""
                    select exam_session_id,session_date,room,type
                    from exam_session 
                    where course_id=?""");
            psmt.setString(1, courseId);
            ResultSet rs = psmt.executeQuery();
            List<ExamSession> examSessions = new ArrayList<ExamSession>();
            while(rs.next()){
                ExamSession examSession = new ExamSession();
                examSession.setId(rs.getInt("exam_session_id"));
                examSession.setDate(rs.getDate("session_date"));
                examSession.setRoom(rs.getString("room"));
                examSession.setType(rs.getString("type"));
                examSessions.add(examSession);
            }
            return examSessions;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ExamSession getExamSessionById(int examSessionId) {
        try {
            PreparedStatement psmt = connection.prepareStatement("""
        select course_id, session_date, room, type
        from exam_session
        where exam_session_id = ?
""");
            psmt.setInt(1, examSessionId);
            ResultSet rs = psmt.executeQuery();
            ExamSession examSession = null;
            if(rs.next()){
                examSession = new ExamSession();
                examSession.setId(examSessionId);
                examSession.setDate(rs.getDate("session_date"));
                examSession.setRoom(rs.getString("room"));
                examSession.setType(rs.getString("type"));
                examSession.setCourseCode(rs.getString("course_id"));
            }
            return examSession;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

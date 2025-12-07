package dao;

import model.Verbal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VerbalDaoImpl implements VerbalDao {
    private Connection connection;
    public VerbalDaoImpl(Connection connection) {
        this.connection = connection;
    }

    /**
     * @param verbal
     */
    @Override
    public void createVerbal(Verbal verbal) {
        try{
            PreparedStatement psmt = connection.prepareStatement(
                    """
            INSERT INTO exam_verbal
                (exam_session_id,creation_timestamp, professor_id) VALUES
            (?, ?, ?);
""");
            psmt.setInt(1, verbal.getExamSessionId());
            psmt.setTimestamp(2, verbal.getCreationTimestamp());
            psmt.setString(3, verbal.getProfessorId());
            psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param verbalId
     * @return
     */
    @Override
    public Verbal getVerbal(int verbalId) {
        try{
            PreparedStatement psmt = connection.prepareStatement("""
        select exam_session_id, creation_timestamp, professor_id 
        from exam_verbal 
        where exam_verbal_id = ?
""");
            psmt.setInt(1, verbalId);
            ResultSet rs = psmt.executeQuery();
            Verbal verbal = null;
            if(rs.next()) {
                verbal = new Verbal();
                verbal.setExamSessionId(rs.getInt("exam_session_id"));
                verbal.setCreationTimestamp(rs.getTimestamp("creation_timestamp"));
                verbal.setProfessorId(rs.getString("professor_id"));
                verbal.setExamVerbalId(verbalId);
            }
            return verbal;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

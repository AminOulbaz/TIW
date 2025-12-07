package dao;

import model.ExamGrade;
import model.ExamResult;
import model.ExamStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExamResultDaoImpl implements ExamResultDao {
    private Connection connection;

    public ExamResultDaoImpl(Connection connection) {
        this.connection = connection;
    }

    /**
     * @param examResult
     */
    @Override
    public void updateExamResult(ExamResult examResult) {
        try{
            PreparedStatement psmt = connection.prepareStatement(
                    """
            update exam_result
            set grade = ?, status = ?  
            where exam_session_id = ? and student_id = ?
"""
            );
            psmt.setString(1, examResult.getGrade().getLabel());
            psmt.setString(2, examResult.getStatus().getLabel());
            psmt.setInt(3,examResult.getExamId());
            psmt.setString(4, examResult.getStudentId());
            psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param examSessionId
     * @param studentId
     * @return
     */
    @Override
    public ExamResult getExamResultByExamSessionIdAndStudentId(int examSessionId, String studentId) {
        try{
            PreparedStatement psmt = connection.prepareStatement(
                    """
            select grade, status
            from exam_result
            where exam_session_id = ? and student_id = ?
            """
            );
            psmt.setInt(1,examSessionId);
            psmt.setString(2,studentId);
            ResultSet rs = psmt.executeQuery();
            ExamResult examResult = new ExamResult();
            if(rs.next()) {
                examResult.setStudentId(studentId);
                examResult.setExamId(examSessionId);
                examResult.setGrade(ExamGrade.getExamGrade(rs.getString("grade")));
                examResult.setStatus(ExamStatus.getExamStatus(rs.getString("status")));
            }else{
                throw new RuntimeException("Exam result not found");
            }
            return examResult;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param examSessionId
     * @return
     */
    @Override
    public List<ExamResult> getExamResultsByExamSessionId(int examSessionId) {
        try{
            PreparedStatement psmt = connection.prepareStatement("""
                select student_id, grade, status
                from exam_result
                where exam_session_id = ?
""");
            psmt.setInt(1,examSessionId);
            ResultSet rs = psmt.executeQuery();
            List<ExamResult> examResults = new ArrayList<>();
            while(rs.next()) {
                ExamResult examResult = new ExamResult();
                examResult.setStudentId(rs.getString("student_id"));
                examResult.setExamId(examSessionId);
                examResult.setGrade(ExamGrade.getExamGrade(rs.getString("grade")));
                examResult.setStatus(ExamStatus.getExamStatus(rs.getString("status")));
                examResults.add(examResult);
            }
            return examResults;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

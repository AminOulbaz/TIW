package dao;

import model.ExamEnrollment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExamEnrollmentStudentDaoImpl implements ExamEnrollmentStudentDao {
    private Connection connection;

    public ExamEnrollmentStudentDaoImpl(Connection connection) {
        this.connection = connection;
    }

    /**
     * @param examSessionId
     * @return
     */
    @Override
    public List<ExamEnrollment> getEnrolledStudentsByExamSession(int examSessionId) {
        try{
            PreparedStatement psmt = connection.prepareStatement(
                    """
            select student_id, enrollment_date
            from exam_enrollment 
            where exam_session_id = ?
""");
            psmt.setInt(1, examSessionId);
            ResultSet rs = psmt.executeQuery();
            List<ExamEnrollment> examEnrollments = new ArrayList<ExamEnrollment>();
            while (rs.next()) {
                ExamEnrollment examEnrollment = new ExamEnrollment();
                examEnrollment.setExamSessionId(examSessionId);
                examEnrollment.setStudentId(rs.getString("student_id"));
                examEnrollment.setEnrollmentDate(rs.getDate("enrollment_date"));
                examEnrollments.add(examEnrollment);
            }
            return examEnrollments;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

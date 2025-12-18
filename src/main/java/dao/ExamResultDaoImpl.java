package dao;

import exception.validation.ExamResultNoExistsException;
import model.*;

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
                throw new ExamResultNoExistsException("no exists exam result with this studentId "+studentId+" and examSessionId "+ examSessionId);
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

    @Override
    public List<ExamResult> getExamResultsByStudentId(String studentId) {
        PreparedStatement psmt = null;
        try {
            psmt = connection.prepareStatement("""
            select exam_session_id, grade, status
            from exam_result
            where student_id = ?
    """);
            psmt.setString(1,studentId);
            ResultSet rs = psmt.executeQuery();
            List<ExamResult> examResults = new ArrayList<>();
            while(rs.next()) {
                ExamResult examResult = new ExamResult();
                examResult.setStudentId(studentId);
                examResult.setExamId(rs.getInt("exam_session_id"));
                examResult.setGrade(ExamGrade.getExamGrade(rs.getString("grade")));
                examResult.setStatus(ExamStatus.getExamStatus(rs.getString("status")));
                examResults.add(examResult);
            }
            return examResults;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insertExamResult(ExamResult examResult) {
        try{
            PreparedStatement psmt = connection.prepareStatement("""
            insert into exam_result(exam_session_id, student_id, grade, status) values 
           (?,?,?,?)
""");
            psmt.setInt(1,examResult.getExamId());
            psmt.setString(2, examResult.getStudentId());
            psmt.setString(3,examResult.getGrade().getLabel());
            psmt.setString(4,examResult.getStatus().getLabel());
            psmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteExamResult(int examSessionId, String studentId) {
        try {
            PreparedStatement psmt = connection.prepareStatement("""
        delete from exam_result where exam_session_id = ? and student_id = ?
""");
            System.out.println("DELETE exam_result WHERE exam_session_id = "
                    + examSessionId + " AND student_id = " + studentId);

            psmt.setInt(1, examSessionId);
            psmt.setString(2, studentId);
            int rows = psmt.executeUpdate();
            System.out.println("Righe eliminate: " + rows);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Student> getStudentsByExamSessionId(int examSessionId) {
        try {
            PreparedStatement psmt = connection.prepareStatement("""
                        select student_id, first_name, last_name, email, degree_program_code
                        from exam_result join student on exam_result.student_id = student.student_id
                        where exam_session_id = ?
                """);
            psmt.setInt(1,examSessionId);
            ResultSet rs = psmt.executeQuery();
            List<Student> students = new ArrayList<>();
            while(rs.next()) {
                Student student = new Student();
                student.setId(rs.getString("student_id"));
                student.setName(rs.getString("first_name"));
                student.setSurname(rs.getString("last_name"));
                student.setEmail(rs.getString("email"));
                student.setDegreeProgramCode(rs.getString("degree_program_code"));
                students.add(student);
            }
            return students;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<ExamResultWithStudent> processPreparedStatementForExamResultWithStudent(PreparedStatement psmt) throws SQLException {
        ResultSet rs = psmt.executeQuery();
        List<ExamResultWithStudent> examResultWithStudents = new ArrayList<>();
        while(rs.next()) {
            Student student = new Student();
            student.setId(rs.getString("student_id"));
            student.setName(rs.getString("first_name"));
            student.setSurname(rs.getString("last_name"));
            student.setEmail(rs.getString("email"));
            student.setDegreeProgramCode(rs.getString("degree_program_code"));
            ExamResult examResult = new ExamResult();
            examResult.setStudentId(student.getId());
            examResult.setExamId(rs.getInt("exam_session_id"));
            examResult.setGrade(ExamGrade.getExamGrade(rs.getString("grade")));
            examResult.setStatus(ExamStatus.getExamStatus(rs.getString("status")));
            examResultWithStudents.add(
                    new ExamResultWithStudent(
                            student, examResult
                    )
            );
        }
        return examResultWithStudents;
    }

    @Override
    public List<ExamResultWithStudent> getExamResultWithStudentsByExamSessionId(int examSessionId) {
        try {
            PreparedStatement psmt = connection.prepareStatement("""
                        select student.student_id, exam_session_id, first_name, last_name, email, degree_program_code, grade, status
                        from exam_result join student on exam_result.student_id = student.student_id
                        where exam_session_id = ?
                """);
            psmt.setInt(1,examSessionId);
            return processPreparedStatementForExamResultWithStudent(psmt);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ExamResultWithStudent> getExamResultWithStudentsByExamSessionId(int examSessionId
            , String sortKey, String sortOrder) {
        try {
            String sql =
                    "SELECT student.student_id, exam_session_id, first_name, last_name, email, degree_program_code, grade, status " +
                            "FROM exam_result JOIN student ON exam_result.student_id = student.student_id " +
                            "WHERE exam_session_id = ? " +
                            "ORDER BY " + sortKey + " " + sortOrder;
            PreparedStatement psmt = connection.prepareStatement(sql);
            psmt.setInt(1, examSessionId);
            System.out.println(psmt);
            return processPreparedStatementForExamResultWithStudent(psmt);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ExamResultWithStudent getExamResultWithStudentByExamSessionIdAndStudentId(int examSessionId, String studentId) {
        try {
            PreparedStatement psmt = connection.prepareStatement("""
                        select student.student_id, first_name, last_name, email, degree_program_code, grade, status
                        from exam_result join student on exam_result.student_id = student.student_id
                        where exam_session_id = ? and exam_result.student_id = ?
                """);
            psmt.setInt(1, examSessionId);
            psmt.setString(2, studentId);
            ResultSet rs = psmt.executeQuery();
            ExamResultWithStudent examResultWithStudent = null;
            if (rs.next()) {
                Student student = new Student();
                student.setId(rs.getString("student_id"));
                student.setName(rs.getString("first_name"));
                student.setSurname(rs.getString("last_name"));
                student.setEmail(rs.getString("email"));
                student.setDegreeProgramCode(rs.getString("degree_program_code"));
                ExamResult examResult = new ExamResult();
                examResult.setStudentId(student.getId());
                examResult.setExamId(examSessionId);
                examResult.setGrade(ExamGrade.getExamGrade(rs.getString("grade")));
                examResult.setStatus(ExamStatus.getExamStatus(rs.getString("status")));
                examResultWithStudent = new ExamResultWithStudent(
                        student, examResult
                );
            }
            return examResultWithStudent;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

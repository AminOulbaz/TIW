package dao;

import model.Course;
import model.Professor;
import model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class StudentDaoImpl implements StudentDao {

    private Connection connection;

    public StudentDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Student getStudentByUserId(String username) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("""
                select *
                from student 
                where username = ?
        """);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                Student student = new Student();
                student.setId(resultSet.getString("student_id"));
                student.setName(resultSet.getString("first_name"));
                student.setSurname(resultSet.getString("last_name"));
                student.setEmail(resultSet.getString("email"));
                student.setDegreeProgramCode(resultSet.getString("degree_program_code"));
                student.setYearOfStudy(resultSet.getInt("year_of_study"));
                return student;
            }
            throw new RuntimeException("doesn't exist a professor with this username:"+username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Student getStudentById(String id) {
        return null;
    }

    @Override
    public List<Student> getStudents() {
        try{
            PreparedStatement psmt = connection.prepareStatement("""
            select * 
            from student
""");
            ResultSet rs = psmt.executeQuery();
            List<Student> students = new ArrayList<>();
            while(rs.next()){
                Student student = new Student();
                student.setId(rs.getString("student_id"));
                student.setName(rs.getString("first_name"));
                student.setSurname(rs.getString("last_name"));
                student.setEmail(rs.getString("email"));
                student.setDegreeProgramCode(rs.getString("degree_program_code"));
                student.setYearOfStudy(rs.getInt("year_of_study"));
                students.add(student);
            }
            return students;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

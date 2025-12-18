package dao;

import model.Course;
import model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDaoImpl implements CourseDao {
    private Connection connection;

    public CourseDaoImpl(Connection connection) {
        this.connection = connection;
    }

    /**
     * @param professorId
     * @return
     */
    @Override
    public List<Course> getCoursesByProfessorId(String professorId) {
        try{
            PreparedStatement psmt = connection.prepareStatement(
                    """
                select course_id, name, credits, degree_program_code
                from course
                where professor_id = ?
"""
            );
            psmt.setString(1,professorId);
            ResultSet rs = psmt.executeQuery();
            List<Course> courses = new ArrayList<Course>();
            while(rs.next()){
                Course course = new Course();
                course.setCode(rs.getString("course_id"));
                course.setName(rs.getString("name"));
                course.setCredits(rs.getInt("credits"));
                course.setDegreeProgramCode(rs.getString("degree_program_code"));
                courses.add(course);
            }
            return courses;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Course> getCourseByDegreeCode(String degreeCode) {
        try{
            PreparedStatement psmt = connection.prepareStatement("""
                select course_id, name, credits, professor_id
                from course
                where degree_program_code = ?
""");
            psmt.setString(1, degreeCode);
            ResultSet rs = psmt.executeQuery();
            List<Course> courses = new ArrayList<>();
            while(rs.next()){
                Course course = new Course();
                course.setCode(rs.getString("course_id"));
                course.setName(rs.getString("name"));
                course.setCredits(rs.getFloat("credits"));
                course.setProfessorId(rs.getString("professor_id"));
                course.setDegreeProgramCode(degreeCode);
                courses.add(course);
            }
            return courses;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Course getCourseByCourseCode(String courseCode) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("""
                select *
                from course 
                where course_id = ?
        """);
            preparedStatement.setString(1, courseCode);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                Course course = new Course();
                course.setDegreeProgramCode(resultSet.getString("degree_program_code"));
                course.setName(resultSet.getString("name"));
                course.setProfessorId(resultSet.getString("professor_id"));
                course.setCode(courseCode);
                course.setCredits(resultSet.getFloat("credits"));
                return course;
            }
            throw new RuntimeException("doesn't exist a course with this id:"+courseCode);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

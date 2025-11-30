package dao;

import model.Course;

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
                select course_id, name, credits, degree_program_id
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
                course.setDegreeProgramId(rs.getInt("degree_program_id"));
                courses.add(course);
            }
            return courses;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param courseId
     * @return
     */
    @Override
    public Course getCourseById(int courseId) {
        return null;
    }
}

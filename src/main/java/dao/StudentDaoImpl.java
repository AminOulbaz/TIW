package dao;

import model.Course;
import model.Student;

import java.sql.Connection;
import java.util.List;

public class StudentDaoImpl implements StudentDao {
    private Connection connection;

    public StudentDaoImpl(Connection connection) {
        this.connection = connection;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Student getStudentById(String id) {
        return null;
    }

    /**
     * @param student
     * @return
     */
    @Override
    public List<Course> getCoursesByStudent(Student student) {
        return List.of();
    }
}

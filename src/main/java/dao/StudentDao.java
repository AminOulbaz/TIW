package dao;

import model.Course;
import model.Student;

import java.util.List;

public interface StudentDao {
    public Student getStudentById(String id);
    public List<Course> getCoursesByStudent(Student student);
}

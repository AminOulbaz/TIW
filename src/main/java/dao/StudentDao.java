package dao;

import model.Course;
import model.Student;

import java.util.List;

public interface StudentDao {
    public Student getStudentByUserId(String username);
    public Student getStudentById(String id);
    public List<Student> getStudents();
}

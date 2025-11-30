package dao;

import model.Course;

import java.util.List;

public interface CourseDao {
    List<Course> getCoursesByProfessorId(String professorId);
    Course getCourseById(int courseId);
}

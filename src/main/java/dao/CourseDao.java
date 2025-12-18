package dao;

import model.Course;

import java.util.List;

public interface CourseDao {
    List<Course> getCoursesByProfessorId(String professorId);
    List<Course> getCourseByDegreeCode(String degreeCode);
    Course getCourseByCourseCode(String courseId);
}

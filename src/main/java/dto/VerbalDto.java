package dto;

import java.sql.Timestamp;
import java.util.List;

public class VerbalDto {
    String examVerbalId;
    String creationTimestamp;
    String examDate;
    String courseCode;
    List<StudentVerbalDto> students;

    public List<StudentVerbalDto> getStudents() {
        return students;
    }

    public void setStudents(List<StudentVerbalDto> students) {
        this.students = students;
    }

    public String getExamVerbalId() {
        return examVerbalId;
    }

    public void setExamVerbalId(String examVerbalId) {
        this.examVerbalId = examVerbalId;
    }

    public String getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(String creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public String getExamDate() {
        return examDate;
    }

    public void setExamDate(String examDate) {
        this.examDate = examDate;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
}

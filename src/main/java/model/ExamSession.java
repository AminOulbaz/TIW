package model;

import java.util.Date;

public class ExamSession {
    private int id;
    private String courseCode;
    private Date date;
    private String room;
    private String type;

    public int getId() {
        return id;
    }

    public ExamSession(){}
    public ExamSession(int id, String courseCode, Date date, String room, String type) {
        this.id = id;
        this.courseCode = courseCode;
        this.date = date;
        this.room = room;
        this.type = type;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public Date getDate() {
        return date;
    }

    public String getRoom() {
        return room;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setId(int id) {
        this.id = id;
    }
}

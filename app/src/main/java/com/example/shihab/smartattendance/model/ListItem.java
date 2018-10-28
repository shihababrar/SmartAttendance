package com.example.shihab.smartattendance.model;

public class ListItem {
    private  String courseId,courseName;

    public ListItem(String courseId, String courseName) {
        this.courseId = courseId;
        this.courseName = courseName;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }
}

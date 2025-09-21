package com.ratemycampus.dto;

public class TeacherCourseDTO {
    public Long id;
    public Integer teacherId;
    public Integer courseId;
    
    // Optional fields for displaying related information
    public String teacherName;
    public String courseName;
    
    public TeacherCourseDTO() {
    }
    
    public TeacherCourseDTO(Long id, Integer teacherId, Integer courseId) {
        this.id = id;
        this.teacherId = teacherId;
        this.courseId = courseId;
    }
}
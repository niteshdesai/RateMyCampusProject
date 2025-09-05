package com.ratemycampus.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class RatingTeacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not exceed 5")
    private int score;

    @NotNull(message = "Teacher is required")
    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    @JsonBackReference(value = "rating-teacher")
    private Teacher teacher;

    @NotNull(message = "Student is required")
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    @JsonBackReference(value = "rating-student")
    private Student student;

    @NotNull(message = "Course is required")
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    @JsonBackReference(value = "rating-course")
    private Course course;

    // Constructors
    public RatingTeacher() {
    }

    public RatingTeacher(int score, Teacher teacher, Student student, Course course) {
        this.score = score;
        this.teacher = teacher;
        this.student = student;
        this.course = course;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @JsonProperty("courseId")
    public Integer getCourseId() {
        return course != null ? course.getC_id() : null;
    }

    @JsonProperty("courseId")
    public void setCourseId(Integer courseId) {
        if (courseId == null) {
            this.course = null;
        } else {
            Course c = new Course();
            c.setC_id(courseId);
            this.course = c;
        }
    }
}

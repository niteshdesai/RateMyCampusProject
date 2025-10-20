package com.ratemycampus.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class TeacherRatingCriteria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Teacher is required")
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @NotNull(message = "Student is required")
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @Min(value = 1, message = "Subject knowledge rating must be at least 1")
    @Max(value = 5, message = "Subject knowledge rating must not exceed 5")
    private int subjectKnowledge;

    @Min(value = 1, message = "Communication skills rating must be at least 1")
    @Max(value = 5, message = "Communication skills rating must not exceed 5")
    private int communicationSkills;

    // Constructor
    public TeacherRatingCriteria() {
    }

    public TeacherRatingCriteria(Teacher teacher, Student student, int subjectKnowledge, int communicationSkills) {
        this.teacher = teacher;
        this.student = student;
        this.subjectKnowledge = subjectKnowledge;
        this.communicationSkills = communicationSkills;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getSubjectKnowledge() {
        return subjectKnowledge;
    }

    public void setSubjectKnowledge(int subjectKnowledge) {
        this.subjectKnowledge = subjectKnowledge;
    }

    public int getCommunicationSkills() {
        return communicationSkills;
    }

    public void setCommunicationSkills(int communicationSkills) {
        this.communicationSkills = communicationSkills;
    }

    @Override
    public String toString() {
        return "TeacherRatingCriteria{" +
                "id=" + id +
                ", subjectKnowledge=" + subjectKnowledge +
                ", communicationSkills=" + communicationSkills +
                '}';
    }
}

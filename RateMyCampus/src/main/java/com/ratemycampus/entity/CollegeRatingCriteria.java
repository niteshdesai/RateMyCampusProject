package com.ratemycampus.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class CollegeRatingCriteria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "College is required")
    @ManyToOne
    @JoinColumn(name = "college_id")
    private College college;

    @NotNull(message = "Student is required")
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @Min(value = 1, message = "Extracurricular activities rating must be at least 1")
    @Max(value = 5, message = "Extracurricular activities rating must not exceed 5")
    private int extracurricularActivities;

    @Min(value = 1, message = "Sports facilities rating must be at least 1")
    @Max(value = 5, message = "Sports facilities rating must not exceed 5")
    private int sportsFacilities;

    @Min(value = 1, message = "Campus facilities rating must be at least 1")
    @Max(value = 5, message = "Campus facilities rating must not exceed 5")
    private int campusFacilities;

    // Constructor
    public CollegeRatingCriteria() {
    }

    public CollegeRatingCriteria(College college, Student student, int extracurricularActivities, 
                               int sportsFacilities, int campusFacilities) {
        this.college = college;
        this.student = student;
        this.extracurricularActivities = extracurricularActivities;
        this.sportsFacilities = sportsFacilities;
        this.campusFacilities = campusFacilities;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public College getCollege() {
        return college;
    }

    public void setCollege(College college) {
        this.college = college;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public int getExtracurricularActivities() {
        return extracurricularActivities;
    }

    public void setExtracurricularActivities(int extracurricularActivities) {
        this.extracurricularActivities = extracurricularActivities;
    }

    public int getSportsFacilities() {
        return sportsFacilities;
    }

    public void setSportsFacilities(int sportsFacilities) {
        this.sportsFacilities = sportsFacilities;
    }

    public int getCampusFacilities() {
        return campusFacilities;
    }

    public void setCampusFacilities(int campusFacilities) {
        this.campusFacilities = campusFacilities;
    }

    @Override
    public String toString() {
        return "CollegeRatingCriteria{" +
                "id=" + id +
                ", extracurricularActivities=" + extracurricularActivities +
                ", sportsFacilities=" + sportsFacilities +
                ", campusFacilities=" + campusFacilities +
                '}';
    }
}

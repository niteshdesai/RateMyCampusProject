package com.ratemycampus.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not exceed 5")
    private int score;

    @NotNull(message = "College is required")
    @ManyToOne
    @JoinColumn(name = "college_id", nullable = false)
    @JsonBackReference(value = "rating-college")
    private College college;

    @NotNull(message = "Student is required")
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    @JsonBackReference(value = "rating-student")
    private Student student;

    // Constructor
   

    public Rating() {
    }

    public Rating(
			@Min(value = 1, message = "Rating must be at least 1") @Max(value = 5, message = "Rating must not exceed 5") int score,
			@NotNull(message = "College is required") College college,
			@NotNull(message = "Student is required") Student student) {
		super();
		this.score = score;
		this.college = college;
		this.student = student;
	}

	// Getters and setters
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

    @Override
    public String toString() {
        return "Rating [id=" + id + ", score=" + score + "]";
    }
}
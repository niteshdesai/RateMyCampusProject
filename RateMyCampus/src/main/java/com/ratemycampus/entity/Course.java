package com.ratemycampus.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer c_id;

    @NotBlank(message = "Course name is required")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Course name must contain only letters and spaces")
    @Size(max = 154)
    private String cName;

    @NotNull(message = "Duration is required")
    private Integer cDuration;

    @NotNull(message = "Since year is required")
    private Integer cSince;

    @ManyToOne
    @JoinColumn(name = "cid")
    @JsonBackReference(value = "college-courses")
    private College college;

    @ManyToOne
    @JoinColumn(name = "department_id")
    @JsonBackReference(value = "department-courses")
    private Department department;

    // Constructor
    
    public Course() {
    }


	public Course(
			@NotBlank(message = "Course name is required") @Pattern(regexp = "^[A-Za-z ]+$", message = "Course name must contain only letters and spaces") @Size(max = 154) String cName,
			@NotNull(message = "Duration is required") Integer cDuration,
			@NotNull(message = "Since year is required") Integer cSince, College college, Department department) {
		super();
		this.cName = cName;
		this.cDuration = cDuration;
		this.cSince = cSince;
		this.college = college;
		this.department = department;
	}


	// Getters and setters
    public Integer getC_id() {
        return c_id;
    }

    public void setC_id(Integer c_id) {
        this.c_id = c_id;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public Integer getcDuration() {
        return cDuration;
    }

    public void setcDuration(Integer cDuration) {
        this.cDuration = cDuration;
    }

    public Integer getcSince() {
        return cSince;
    }

    public void setcSince(Integer cSince) {
        this.cSince = cSince;
    }
 

    public College getCollege() {
        return college;
    }

    public void setCollege(College college) {
        this.college = college;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    
}
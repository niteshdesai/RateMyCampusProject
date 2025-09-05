package com.ratemycampus.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tid;

    @NotBlank(message = "Teacher name is required")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Teacher name must contain only letters and spaces")
    private String tname;

    @NotNull(message = "Semester is required")
    private Integer tsem;

    @NotBlank(message = "Section is required")
    @Pattern(regexp = "^[A-Z]$", message = "Section must be a single capital letter")
    private String tsection;

    private String timg;

    @NotNull(message = "College is required")
    @ManyToOne
    @JoinColumn(name = "cid")
    @JsonBackReference(value = "teacher-college")
    private College college;

    @NotNull(message = "Department is required")
    @ManyToOne
    @JoinColumn(name = "department_id")
    @JsonBackReference(value = "teacher-department")
    private Department department;

    // Constructor
    public Teacher() {
    }

    public Teacher(
            @NotBlank(message = "Teacher name is required") @Pattern(regexp = "^[A-Za-z ]+$", message = "Teacher name must contain only letters and spaces") String tname,
            @NotNull(message = "Semester is required") Integer tsem,
            @NotBlank(message = "Section is required") @Pattern(regexp = "^[A-Z]$", message = "Section must be a single capital letter") String tsection,
            @NotBlank(message = "Subject is required") @Size(max = 45) String tsubject,
            String timg,
            College college,
            Department department) {
        this.tname = tname;
        this.tsem = tsem;
        this.tsection = tsection;
        this.timg = timg;
        this.college = college;
        this.department = department;
    }

    // Getters and setters
    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public Integer getTsem() {
        return tsem;
    }

    public void setTsem(Integer tsem) {
        this.tsem = tsem;
    }

    public String getTsection() {
        return tsection;
    }

    public void setTsection(String tsection) {
        this.tsection = tsection;
    }


    public String getTimg() {
        return timg;
    }

    public void setTimg(String timg) {
        this.timg = timg;
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

    @Override
    public String toString() {
        return "Teacher [tid=" + tid + ", tname=" + tname + ", tsem=" + tsem + ",timg=" + timg + "]";
    }
}
package com.ratemycampus.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sid;

    @NotBlank(message = "Enrollment number is required")
    @Size(max = 145)
    private String enrollment;

    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Student name must contain only letters and spaces")
    @Size(max = 145)
    private String sname;

    @NotNull(message = "Semester is required")
    private Integer ssem;

    @NotBlank(message = "Section is required")
    @Pattern(regexp = "^[A-Z]$", message = "Section must be a single capital letter")
    private String ssection;

    @NotBlank(message = "Gender is required")
    @Pattern(regexp = "^(Male|Female|Other)$", message = "Gender must be Male, Female, or Other")
    private String sgender;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "\\d{10}", message = "Mobile must be 10 digits")
    private String smobile;

    @NotBlank(message = "City is required")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "City must contain only letters and spaces")
    private String scity;

    private String simg;

    @Email(message = "Invalid email format")
    private String semail;

    @NotNull(message = "College is required")
    @ManyToOne
    @JoinColumn(name = "cid")
    @JsonBackReference(value = "student-college")
    private College college;

    @NotNull(message = "Department is required")
    @ManyToOne
    @JoinColumn(name = "department_id")
    @JsonBackReference(value = "student-department")
    private Department department;

    @NotNull(message = "Course is required")
    @ManyToOne
    @JoinColumn(name = "sc_id")
    @JsonBackReference(value = "student-course")
    private Course course;

    // Constructor
    public Student() {
    }

    public Student(
            @NotBlank(message = "Enrollment number is required") @Size(max = 145) String enrollment,
            @NotBlank(message = "Name is required") @Pattern(regexp = "^[A-Za-z ]+$", message = "Student name must contain only letters and spaces") @Size(max = 145) String sname,
            @NotNull(message = "Semester is required") Integer ssem,
            @NotBlank(message = "Section is required") @Pattern(regexp = "^[A-Z]$", message = "Section must be a single capital letter") String ssection,
            @NotBlank(message = "Gender is required") @Pattern(regexp = "^(Male|Female|Other)$", message = "Gender must be Male, Female, or Other") String sgender,
            @NotBlank(message = "Mobile number is required") @Pattern(regexp = "\\d{10}", message = "Mobile must be 10 digits") String smobile,
            @NotBlank(message = "City is required") @Pattern(regexp = "^[A-Za-z ]+$", message = "City must contain only letters and spaces") String scity,
            String simg,
            @Email(message = "Invalid email format") String semail,
            College college,
            Department department,
            Course course) {
        this.enrollment = enrollment;
        this.sname = sname;
        this.ssem = ssem;
        this.ssection = ssection;
        this.sgender = sgender;
        this.smobile = smobile;
        this.scity = scity;
        this.simg = simg;
        this.semail = semail;
        this.college = college;
        this.department = department;
        this.course = course;
    }

    // Getters and setters
    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public String getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(String enrollment) {
        this.enrollment = enrollment;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public Integer getSsem() {
        return ssem;
    }

    public void setSsem(Integer ssem) {
        this.ssem = ssem;
    }

    public String getSsection() {
        return ssection;
    }

    public void setSsection(String ssection) {
        this.ssection = ssection;
    }

    public String getSgender() {
        return sgender;
    }

    public void setSgender(String sgender) {
        this.sgender = sgender;
    }

    public String getSmobile() {
        return smobile;
    }

    public void setSmobile(String smobile) {
        this.smobile = smobile;
    }

    public String getScity() {
        return scity;
    }

    public void setScity(String scity) {
        this.scity = scity;
    }

    public String getSimg() {
        return simg;
    }

    public void setSimg(String simg) {
        this.simg = simg;
    }

    public String getSemail() {
        return semail;
    }

    public void setSemail(String semail) {
        this.semail = semail;
    }

    public College getCollege() {
        return college;
    }

    public void setCollege(College college) {
        this.college = college;
    }

    @JsonProperty("collegeId")
    public void setCollegeId(Long collegeId) {
        if (collegeId != null) {
            this.college = new College();
            this.college.setCid(collegeId);
        } else {
            this.college = null;
        }
    }

    @JsonProperty("collegeId")
    public Long getCollegeId() {
        return college != null ? college.getCid() : null;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @JsonProperty("deptId")
    public void setDeptId(Long deptId) {
        if (deptId != null) {
            this.department = new Department();
            this.department.setDeptId(deptId);
        } else {
            this.department = null;
        }
    }

    @JsonProperty("deptId")
    public Long getDeptId() {
        return department != null ? department.getDeptId() : null;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @JsonProperty("courseId")
    public void setCourseId(Integer courseId) {
        if (courseId != null) {
            this.course = new Course();
            this.course.setC_id(courseId);
        } else {
            this.course = null;
        }
    }

    @JsonProperty("courseId")
    public Integer getCourseId() {
        return course != null ? course.getC_id() : null;
    }

    @Override
    public String toString() {
        return "Student [sid=" + sid + ", enrollment=" + enrollment + ", sname=" + sname + ", ssem=" + ssem
                + ", ssection=" + ssection + ", sgender=" + sgender + ", smobile=" + smobile + ", scity=" + scity
                + ", simg=" + simg + ", semail=" + semail + "]";
    }
}
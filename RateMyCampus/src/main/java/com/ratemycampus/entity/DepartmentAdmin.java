package com.ratemycampus.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class DepartmentAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer hodId;

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 30, message = "Username must be between 4 and 30 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username must contain only letters, numbers, and underscores")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "HOD name is required")
    @Pattern(regexp = "^[A-Za-z. ]+$", message = "HOD name must contain only letters and dots")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

     private String daImg;
     
    @NotNull(message = "Department is required")
    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    @JsonBackReference(value = "deptadmin-department")
    private Department department;

    @NotNull(message = "College is required")
    @ManyToOne
    @JoinColumn(name = "cid", nullable = false)
    @JsonBackReference(value = "deptadmin-college")
    private College college;

    // Constructor


    public DepartmentAdmin() {
    }

   

	public DepartmentAdmin(
			@NotBlank(message = "Username is required") @Size(min = 4, max = 30, message = "Username must be between 4 and 30 characters") @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username must contain only letters, numbers, and underscores") String username,
			@NotBlank(message = "Password is required") @Size(min = 6, message = "Password must be at least 6 characters") String password,
			@NotBlank(message = "HOD name is required") @Pattern(regexp = "^[A-Za-z. ]+$", message = "HOD name must contain only letters and dots") String name,
			@NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email,
			String daImg, @NotNull(message = "Department is required") Department department,
			@NotNull(message = "College is required") College college) {
		super();
		this.username = username;
		this.password = password;
		this.name = name;
		this.email = email;
		this.daImg = daImg;
		this.department = department;
		this.college = college;
	}



	// Getters and setters
    public Integer getHodId() {
        return hodId;
    }

    public void setHodId(Integer hodId) {
        this.hodId = hodId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public College getCollege() {
        return college;
    }

    public void setCollege(College college) {
        this.college = college;
    }



	@Override
	public String toString() {
		return "DepartmentAdmin [hodId=" + hodId + ", username=" + username + ", password=" + password + ", name="
				+ name + ", email=" + email + ", daImg=" + daImg + ", department=" + department + ", college=" + college
				+ "]";
	}



	public String getDaImg() {
		return daImg;
	}



	public void setDaImg(String daImg) {
		this.daImg = daImg;
	}
   
}
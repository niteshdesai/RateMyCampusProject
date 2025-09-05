package com.ratemycampus.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;


@Entity
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deptId;

    @NotBlank(message = "Department name is required")
    private String deptName;
   
    @Column(name = "description")
    private String desc;
    
    @NotNull(message = "College is required")
    @ManyToOne
    @JoinColumn(name = "cid")
    @JsonBackReference(value = "college-departments")
    private College college;

	// Getters and setters
    public Long getDeptId() {
        return deptId;
    }

    public Department() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Department(Long deptId, @NotBlank(message = "Department name is required") String deptName, String desc,
			@NotNull(message = "College is required") College college) {
		super();
		this.deptId = deptId;
		this.deptName = deptName;
		this.desc = desc;
		this.college = college;
	}

	public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }


    public College getCollege() {
        return college;
    }

    public void setCollege(College college) {
        this.college = college;
    }


    

	@Override
	public String toString() {
		return "Department [deptId=" + deptId + ", deptName=" + deptName + ", desc=" + desc + ", college=" + college
				+ "]";
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
package com.ratemycampus.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Admin name is required")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Name must contain only letters")
    private String name;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;



    public Admin() {
    }

    public Admin(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
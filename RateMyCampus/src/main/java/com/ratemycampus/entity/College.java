package com.ratemycampus.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;


@Entity
public class College {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cid;

    @NotBlank(message = "College name is required")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "College name must contain only letters and spaces")
    @Size(max = 145)
    private String cname;

    @Lob
    private String cdesc;

    @Lob
    @NotBlank(message = "Activity is required")
    private String cactivity;

    @NotBlank(message = "Address is required")
    private String address;

    @Lob
    private String cimg;

    @Email(message = "Invalid email format")
    private String email;

	// Getters and setters
    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCdesc() {
        return cdesc;
    }

    public void setCdesc(String cdesc) {
        this.cdesc = cdesc;
    }

    public String getCactivity() {
        return cactivity;
    }

    public void setCactivity(String cactivity) {
        this.cactivity = cactivity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCimg() {
        return cimg;
    }

    public void setCimg(String cimg) {
        this.cimg = cimg;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public String toString() {
        return "College [cid=" + cid + ", cname=" + cname + ", cdesc=" + cdesc + ", cactivity=" + cactivity
                + ", address=" + address + ", cimg=" + cimg + ", email=" + email + "]";
    }
}
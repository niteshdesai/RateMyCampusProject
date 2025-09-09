package com.ratemycampus.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import com.ratemycampus.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ratemycampus.repository.CollegeRepository;
import com.ratemycampus.repository.DepartmentRepository;
import com.ratemycampus.repository.RatingRepository;
import com.ratemycampus.entity.*;

@Service
public class CollegeService {
    
	@Autowired
    private final StudentRepository studentRepository;

	@Autowired
    private final DepartmentRepository departmentRepository;

    @Autowired
    private CollegeRepository collegeRepository;
    
    

    @Autowired
    private RatingRepository ratingRepository;

    CollegeService(DepartmentRepository departmentRepository, StudentRepository studentRepository) {
        this.departmentRepository = departmentRepository;
        this.studentRepository = studentRepository;
    }

    public List<College> getAllColleges() {
        return collegeRepository.findAll();
    }

    public College getCollegeById(Long id) {
        return collegeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("College not found"));
    }

    public College createCollege(College college) {
    if (collegeRepository.existsByCname(college.getCname())) {
            throw new RuntimeException("College with name already exists");
        }

        if (collegeRepository.existsByEmail(college.getEmail())) {
            throw new RuntimeException("College with email already exists");
        }

    return collegeRepository.save(college);
    }

    public College updateCollege(Long id, College updatedCollege) {
        College existing = getCollegeById(id);

        // Delete old image if it exists and a new image is provided
        if (updatedCollege.getCimg() != null && existing.getCimg() != null) {
            try {
                Files.deleteIfExists(Paths.get(existing.getCimg()));
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete old image: " + e.getMessage());
            }
        }

    existing.setCname(updatedCollege.getCname());
    existing.setCdesc(updatedCollege.getCdesc());
    existing.setCactivity(updatedCollege.getCactivity());
    existing.setAddress(updatedCollege.getAddress());
    existing.setEmail(updatedCollege.getEmail());
    existing.setCimg(updatedCollege.getCimg());
    existing.setPhone(updatedCollege.getPhone());
    existing.setWebsite(updatedCollege.getWebsite());
        return collegeRepository.save(existing);
    }

    public void deleteCollege(Long id) {
        College college = getCollegeById(id);
        if (college.getCimg() != null) {
            try {
                Files.deleteIfExists(Paths.get(college.getCimg()));
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete image: " + e.getMessage());
            }
        }
        collegeRepository.delete(college);
    }

    public List<College> searchCollegesByName(String name) {
        return collegeRepository.findByCnameContainingIgnoreCase(name);
    }

    public List<Department> getDepartmentsByCollege(Long collegeId) {
        College college = getCollegeById(collegeId);
        return departmentRepository.findByCollegeCid(college.getCid());
    }

    public List<Rating> getRatingsByCollege(Long collegeId) {
        return ratingRepository.findByCollegeCid(collegeId);
    }

    public List<College> getCollegesByCity(String city) {
        return collegeRepository.findByAddressContainingIgnoreCase(city);
    }
    public long getRatingCountByCollegeId(Long collegeId) {
        return ratingRepository.countDistinctStudentsByCollegeCid(collegeId);
    }
    
    public long getStudentCountByCollegeId(Long collegeId) {
        return studentRepository.countByCollegeCid(collegeId);
    }
   
}
package com.ratemycampus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ratemycampus.entity.DepartmentAdmin;
import com.ratemycampus.repository.DepartmentAdminRepository;

@Service
	public class DepartmentAdminService {

	    @Autowired
	    private DepartmentAdminRepository repository;



	    public DepartmentAdmin getHodById(Long id) {
	        return repository.findById(id).orElse(null);
	    }
	    
	    public DepartmentAdmin createHod(DepartmentAdmin hod) {
	        if (repository.existsByUsername(hod.getUsername())) {
	            throw new RuntimeException("HOD with username already exists");
	        }
	        if (repository.existsByEmail(hod.getEmail())) {
	            throw new RuntimeException("HOD with email already exists");
	        }
	        return repository.save(hod);
	    }
	    
	    public DepartmentAdmin getHodById(Integer id) {

	        return repository.findByHodId(id)
	                .orElseThrow(() -> new RuntimeException("HOD not found with ID: " + id));
	    }

	    public DepartmentAdmin updateHod(Integer id, DepartmentAdmin updatedHod) {
	    	DepartmentAdmin existing = getHodById(id); // check existence

	        // Optional: prevent changing to a username/email that already exists
	        if (!existing.getUsername().equals(updatedHod.getUsername()) &&
	            repository.existsByUsername(updatedHod.getUsername())) {
	            throw new RuntimeException("Username already in use by another HOD");
	        }

	        if (!existing.getEmail().equals(updatedHod.getEmail()) &&
	            repository.existsByEmail(updatedHod.getEmail())) {
	            throw new RuntimeException("Email already in use by another HOD");
	        }

	        existing.setName(updatedHod.getName());
	        existing.setUsername(updatedHod.getUsername());
	        existing.setPassword(updatedHod.getPassword());
	        existing.setEmail(updatedHod.getEmail());
	       
	      
	        existing.setCollege(updatedHod.getCollege());
	        existing.setDepartment(updatedHod.getDepartment());
	     

	        return repository.save(existing);
	    }
	    public String deleteHodById(Integer id) {
	    	DepartmentAdmin existing = getHodById(id); // throws if not found
	        repository.delete(existing);
	        return "HOD with ID " + id + " deleted successfully";
	    }
	    public List<DepartmentAdmin> getAllHods() {

            return repository.findAll();
	    }
	    public DepartmentAdmin getHodByDepartmentId(Long departmentId) {
	        return repository.findByDepartmentDeptId(departmentId).orElse(null);
	    }

		


	}



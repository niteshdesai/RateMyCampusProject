package com.ratemycampus.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ratemycampus.entity.*;
@Repository
public interface CollegeRepository extends JpaRepository<College, Long> {
	 boolean existsByCname(String cname);
	 boolean existsByEmail(String email);
	 List<College> findByCnameContainingIgnoreCase(String name);
	 List<College> findByAddressContainingIgnoreCase(String city);
	 Optional<College> findByCid(Long collegeId);

	

	
	 
	 
	 
}

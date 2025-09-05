package com.ratemycampus.repository;

import com.ratemycampus.entity.CollegeAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CollegeAdminRepository extends JpaRepository<CollegeAdmin, Integer> {
	 boolean existsByEmail(String email);

	 Optional<CollegeAdmin> findByEmail(String email);
	

	 Optional<CollegeAdmin> findById(Long id);

	 CollegeAdmin findByEmailAndPassword(String email, String password);
}

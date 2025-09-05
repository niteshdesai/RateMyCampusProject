package com.ratemycampus.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ratemycampus.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
	Optional<Department> findBydeptName(String dname);
	List<Department> findByCollegeCid(Long cid);
}


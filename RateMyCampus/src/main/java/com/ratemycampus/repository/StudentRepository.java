package com.ratemycampus.repository;


import com.ratemycampus.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
	Optional<Student> findByEnrollment(String enrollmentNumber); // Use underscore
    List<Student> findByCollegeCid(Long collegeId);
    boolean existsByEnrollment(String enrollmentNumber);
	Optional<Student> findBySid(Integer studentId);
	long countByCollegeCid(Long collegeId);
	Student findByEnrollmentAndCollegeCidAndSsem(String enrollment, Long collegeId, Integer semester);
	List<Student> findByDepartmentDeptId(Long departmentId);
	@Query("select s from Student s where s.course.c_id = :courseId")
	List<Student> findByCourseId(@Param("courseId") Integer courseId);
	
}

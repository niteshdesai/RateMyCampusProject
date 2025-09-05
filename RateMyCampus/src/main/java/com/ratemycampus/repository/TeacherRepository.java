package com.ratemycampus.repository;

import com.ratemycampus.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    boolean existsByTid(Integer tid);
    List<Teacher> findByCollegeCid(Long collegeId);
    List<Teacher> findByDepartmentDeptId(Long departmentId);
}

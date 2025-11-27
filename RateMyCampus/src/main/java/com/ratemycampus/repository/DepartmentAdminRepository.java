package com.ratemycampus.repository;

import com.ratemycampus.entity.DepartmentAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface DepartmentAdminRepository extends JpaRepository<DepartmentAdmin, Long> {
    Optional<DepartmentAdmin> findByUsernameAndPassword(String username, String password);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
	Optional<DepartmentAdmin> findByHodId(Integer id);
	Optional<DepartmentAdmin> findByEmail(String email);
	DepartmentAdmin findByEmailAndPassword(String email, String password);
	Optional<DepartmentAdmin> findByDepartmentDeptId(Long departmentId);
	List<DepartmentAdmin> findByCollege_Cid(Long collegeId);

}

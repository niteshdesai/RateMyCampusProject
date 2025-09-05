package com.ratemycampus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ratemycampus.entity.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByCollegeCid(Long collegeId);
    boolean existsByCollege_CidAndStudent_Sid(Long collegeId, Integer studentId);


    Rating findByCollege_CidAndStudent_Sid(Long collegeId, Integer studentId);

    long countDistinctStudentsByCollegeCid(Long collegeId);
}


package com.ratemycampus.repository;

import com.ratemycampus.entity.CollegeRatingCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollegeRatingCriteriaRepository extends JpaRepository<CollegeRatingCriteria, Long> {
    
    List<CollegeRatingCriteria> findByCollegeCid(Long collegeId);
    
    List<CollegeRatingCriteria> findByStudentSid(Long studentId);

    CollegeRatingCriteria findByCollegeCidAndStudentSid(Long collegeId, Long studentId);
    
    @Query("SELECT AVG(c.extracurricularActivities) FROM CollegeRatingCriteria c WHERE c.college.cid = :collegeId")
    Double getAverageExtracurricularActivitiesByCollegeId(@Param("collegeId") Long collegeId);
    
    @Query("SELECT AVG(c.sportsFacilities) FROM CollegeRatingCriteria c WHERE c.college.cid = :collegeId")
    Double getAverageSportsFacilitiesByCollegeId(@Param("collegeId") Long collegeId);
    
    @Query("SELECT AVG(c.campusFacilities) FROM CollegeRatingCriteria c WHERE c.college.cid = :collegeId")
    Double getAverageCampusFacilitiesByCollegeId(@Param("collegeId") Long collegeId);
    
    @Query("SELECT COUNT(DISTINCT c.student.sid) FROM CollegeRatingCriteria c WHERE c.college.cid = :collegeId")
    Long getDistinctStudentCountByCollegeId(@Param("collegeId") Long collegeId);
}

package com.ratemycampus.service;

import com.ratemycampus.entity.CollegeRatingCriteria;
import com.ratemycampus.repository.CollegeRatingCriteriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollegeRatingCriteriaService {

    @Autowired
    private CollegeRatingCriteriaRepository collegeRatingCriteriaRepository;

    public CollegeRatingCriteria addRating(CollegeRatingCriteria rating) {
        return collegeRatingCriteriaRepository.save(rating);
    }

    public  CollegeRatingCriteria CheckRating( Long cid,Long sid)

    {
        return collegeRatingCriteriaRepository.findByCollegeCidAndStudentSid(cid,sid);
    }

    public List<CollegeRatingCriteria> getRatingsByCollege(Long collegeId) {
        return collegeRatingCriteriaRepository.findByCollegeCid(collegeId);
    }

    public List<CollegeRatingCriteria> getRatingsByStudent(Long studentId) {
        return collegeRatingCriteriaRepository.findByStudentSid(studentId);
    }

    public Double getAverageExtracurricularActivitiesByCollegeId(Long collegeId) {
        return collegeRatingCriteriaRepository.getAverageExtracurricularActivitiesByCollegeId(collegeId);
    }

    public Double getAverageSportsFacilitiesByCollegeId(Long collegeId) {
        return collegeRatingCriteriaRepository.getAverageSportsFacilitiesByCollegeId(collegeId);
    }

    public Double getAverageCampusFacilitiesByCollegeId(Long collegeId) {
        return collegeRatingCriteriaRepository.getAverageCampusFacilitiesByCollegeId(collegeId);
    }

    public Long getDistinctStudentCountByCollegeId(Long collegeId) {
        return collegeRatingCriteriaRepository.getDistinctStudentCountByCollegeId(collegeId);
    }

    public List<CollegeRatingCriteria> getAllRatings() {
        return collegeRatingCriteriaRepository.findAll();
    }
}

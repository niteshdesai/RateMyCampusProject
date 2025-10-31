package com.ratemycampus.service;

import com.ratemycampus.entity.CollegeRatingCriteria;
import com.ratemycampus.repository.CollegeRatingCriteriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

    public HashMap<String, Double> getRatingsByCollege(Long collegeId) {

         List<CollegeRatingCriteria>  rating=collegeRatingCriteriaRepository.findByCollegeCid(collegeId);

         double campus_facilitiessum=0;
         double sports_facilitiessum=0;
         double extracurricular_activitiessum=0;

         for( CollegeRatingCriteria cr:rating)
         {
             campus_facilitiessum+=cr.getCampusFacilities();
             sports_facilitiessum+=cr.getSportsFacilities();
             extracurricular_activitiessum+=cr.getExtracurricularActivities();
         }


        HashMap<String,Double> allrating=new HashMap<>();

         allrating.put("campus_facilities",campus_facilitiessum/rating.size());
         allrating.put("sports_facilities",sports_facilitiessum/rating.size());
         allrating.put("extracurricular_activities",extracurricular_activitiessum/rating.size());
         return allrating;

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

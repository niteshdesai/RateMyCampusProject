package com.ratemycampus.service;

import com.ratemycampus.entity.TeacherRatingCriteria;
import com.ratemycampus.repository.TeacherRatingCriteriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherRatingCriteriaService {

    @Autowired
    private TeacherRatingCriteriaRepository teacherRatingCriteriaRepository;


    public TeacherRatingCriteria addRating(TeacherRatingCriteria rating) {

        

        return teacherRatingCriteriaRepository.save(rating);
    }


    public List<TeacherRatingCriteria> getRatingsByTeacher(Integer teacherId) {
        return teacherRatingCriteriaRepository.findByTeacherTid(teacherId);
    }

    public List<TeacherRatingCriteria> getRatingsByStudent(Long studentId) {
        return teacherRatingCriteriaRepository.findByStudentSid(studentId);
    }

    public Double getAverageSubjectKnowledgeByTeacherId(Integer teacherId) {
        return teacherRatingCriteriaRepository.getAverageSubjectKnowledgeByTeacherId(teacherId);
    }

    public Double getAverageCommunicationSkillsByTeacherId(Integer teacherId) {
        return teacherRatingCriteriaRepository.getAverageCommunicationSkillsByTeacherId(teacherId);
    }

    public Long getDistinctStudentCountByTeacherId(Integer teacherId) {
        return teacherRatingCriteriaRepository.getDistinctStudentCountByTeacherId(teacherId);
    }

    public List<TeacherRatingCriteria> getAllRatings() {
        return teacherRatingCriteriaRepository.findAll();
    }

    public TeacherRatingCriteria getRatingsByTeacherAndStudent(Integer teacherId, Integer studentId) {

        return teacherRatingCriteriaRepository.findByTeacherTidAndStudentSid(teacherId, studentId);
    }
}

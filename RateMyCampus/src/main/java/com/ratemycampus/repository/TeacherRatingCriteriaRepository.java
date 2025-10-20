package com.ratemycampus.repository;

import com.ratemycampus.entity.TeacherRatingCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRatingCriteriaRepository extends JpaRepository<TeacherRatingCriteria, Long> {
    
    List<TeacherRatingCriteria> findByTeacherTid(Integer teacherId);
    
    List<TeacherRatingCriteria> findByStudentSid(Long studentId);
    
    @Query("SELECT AVG(t.subjectKnowledge) FROM TeacherRatingCriteria t WHERE t.teacher.tid = :teacherId")
    Double getAverageSubjectKnowledgeByTeacherId(@Param("teacherId") Integer teacherId);
    
    @Query("SELECT AVG(t.communicationSkills) FROM TeacherRatingCriteria t WHERE t.teacher.tid = :teacherId")
    Double getAverageCommunicationSkillsByTeacherId(@Param("teacherId") Integer teacherId);
    
    @Query("SELECT COUNT(DISTINCT t.student.sid) FROM TeacherRatingCriteria t WHERE t.teacher.tid = :teacherId")
    Long getDistinctStudentCountByTeacherId(@Param("teacherId") Integer teacherId);
}

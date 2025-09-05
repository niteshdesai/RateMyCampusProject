package com.ratemycampus.repository;

import com.ratemycampus.entity.RatingTeacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RatingTeacherRepository extends JpaRepository<RatingTeacher, Long> {
    List<RatingTeacher> findByTeacherTid(Long teacherId);
    List<RatingTeacher> findByStudentSid(Long studentId);
    boolean existsByTeacher_TidAndStudent_Sid(Integer TeacherId, Integer studentId);
    RatingTeacher findByTeacher_TidAndStudent_Sid(Integer TeacherId, Integer studentId);
    boolean existsByTeacher_TidAndStudent_SidAndCourse(Integer TeacherId, Integer studentId, com.ratemycampus.entity.Course course);
    RatingTeacher findByTeacher_TidAndStudent_SidAndCourse(Integer TeacherId, Integer studentId, com.ratemycampus.entity.Course course);
}

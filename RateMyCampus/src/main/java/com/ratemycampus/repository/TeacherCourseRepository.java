package com.ratemycampus.repository;

import com.ratemycampus.entity.Course;
import com.ratemycampus.entity.TeacherCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TeacherCourseRepository extends JpaRepository<TeacherCourse, Long> {
    boolean existsByTeacher_TidAndCourse(Integer teacherId, Course course);
    List<TeacherCourse> findByTeacher_Tid(Integer teacherId);
    List<TeacherCourse> findByCourse(Course course);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM TeacherCourse tc WHERE tc.teacher.tid = :teacherId AND tc.course.c_id = :courseId")
    void deleteTeacherCourseAssignment(@Param("teacherId") Integer teacherId, @Param("courseId") Integer courseId);
    
    TeacherCourse findByTeacher_TidAndCourse(Integer teacherId, Course course);
}



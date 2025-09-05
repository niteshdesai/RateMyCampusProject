package com.ratemycampus.repository;

import com.ratemycampus.entity.Course;
import com.ratemycampus.entity.TeacherCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherCourseRepository extends JpaRepository<TeacherCourse, Long> {
    boolean existsByTeacher_TidAndCourse(Integer teacherId, Course course);
    List<TeacherCourse> findByTeacher_Tid(Integer teacherId);
    List<TeacherCourse> findByCourse(Course course);
    void deleteByTeacher_TidAndCourse(Integer teacherId, Course course);
}



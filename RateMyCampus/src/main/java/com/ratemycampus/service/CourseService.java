package com.ratemycampus.service;

import com.ratemycampus.entity.Course;
import com.ratemycampus.exception.ResourceNotFoundException;
import com.ratemycampus.repository.CollegeRepository;
import com.ratemycampus.repository.CourseRepository;
import com.ratemycampus.repository.DepartmentRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CollegeRepository collegeRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    public Course addCourse(Course course) {
        Long collegeId = course.getCollege() != null ? course.getCollege().getCid() : null;
        Long deptId = course.getDepartment() != null ? course.getDepartment().getDeptId() : null;

        if (collegeId == null) {
            throw new ResourceNotFoundException("College ID is required");
        }

        if (deptId == null) {
            throw new ResourceNotFoundException("Department ID is required");
        }

        // Fetch actual referenced entities
        var college = collegeRepository.findById(collegeId)
            .orElseThrow(() -> new ResourceNotFoundException("College not found with ID: " + collegeId));
        var department = departmentRepository.findById(deptId)
            .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + deptId));

        // Set attached references
//        course.setCollege(college);
//        course.setDepartment(department);

//        if (courseRepository.existsByCName(course.getcName())) {
//            throw new IllegalArgumentException("Course with this name already exists");
//        }

        return courseRepository.save(course);
    }


    public Course updateCourse(Integer id, Course updatedCourse) {
        Course existing = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + id));

        existing.setcName(updatedCourse.getcName());
        existing.setcDuration(updatedCourse.getcDuration());
        existing.setcSince(updatedCourse.getcSince());
//        existing.setCollege(updatedCourse.getCollege());
//        existing.setDepartment(updatedCourse.getDepartment());

        return courseRepository.save(existing);
    }

    public void deleteCourse(Integer id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course not found with ID: " + id);
        }
        courseRepository.deleteById(id);
    }

    public Course getCourseById(Integer id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + id));
    }

    public List<Course> getAllCourses() {
        if(courseRepository.findAll().size()!=0)
        {
            return courseRepository.findAll() ;
        }
        else {
            throw new ResourceNotFoundException("Course not found");
        }


    }
}


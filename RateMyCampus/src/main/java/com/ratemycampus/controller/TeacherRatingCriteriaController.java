package com.ratemycampus.controller;

import com.ratemycampus.entity.TeacherRatingCriteria;
import com.ratemycampus.dto.TeacherRatingCriteriaDTO;
import com.ratemycampus.dto.DtoMapper;
import com.ratemycampus.service.TeacherRatingCriteriaService;
import com.ratemycampus.security.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/teacher-rating-criteria")
public class TeacherRatingCriteriaController {

    @Autowired
    private TeacherRatingCriteriaService teacherRatingCriteriaService;

    @Autowired
    private SecurityUtils securityUtils;

    @PostMapping
    public ResponseEntity<?> addRating(@Valid @RequestBody TeacherRatingCriteriaDTO ratingDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                HashMap<String, String> errors = new HashMap<>();
                result.getFieldErrors().forEach(error -> {
                    errors.put(error.getField(), error.getDefaultMessage());
                });
                return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
            }

            // Security check: Ensure the student ID in the request matches the authenticated student
            Integer currentStudentId = securityUtils.getCurrentStudentId();
            if (currentStudentId == null) {
                HashMap<String, String> errors = new HashMap<>();
                errors.put("error", "Authentication required");
                return new ResponseEntity<>(errors, HttpStatus.UNAUTHORIZED);
            }

            if (ratingDTO.studentId == null) {
                HashMap<String, String> errors = new HashMap<>();
                errors.put("error", "Student ID is required");
                return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
            }

            if (!currentStudentId.equals(ratingDTO.studentId.intValue())) {
                HashMap<String, String> errors = new HashMap<>();
                errors.put("error", "You can only submit ratings for yourself");
                return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
            }

            TeacherRatingCriteria rating = DtoMapper.toTeacherRatingCriteriaEntity(ratingDTO);
            TeacherRatingCriteria saved = teacherRatingCriteriaService.addRating(rating);
            TeacherRatingCriteriaDTO savedDTO = DtoMapper.toTeacherRatingCriteriaDTO(saved);
            return new ResponseEntity<>(savedDTO, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            HashMap<String, String> errors = new HashMap<>();
            errors.put("error", e.getMessage());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<TeacherRatingCriteriaDTO>> getRatingsByTeacher(@PathVariable Integer teacherId) {
        List<TeacherRatingCriteria> ratings = teacherRatingCriteriaService.getRatingsByTeacher(teacherId);
        List<TeacherRatingCriteriaDTO> ratingDTOs = ratings.stream()
                .map(DtoMapper::toTeacherRatingCriteriaDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ratingDTOs);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<TeacherRatingCriteriaDTO>> getRatingsByStudent(@PathVariable Long studentId) {
        List<TeacherRatingCriteria> ratings = teacherRatingCriteriaService.getRatingsByStudent(studentId);
        List<TeacherRatingCriteriaDTO> ratingDTOs = ratings.stream()
                .map(DtoMapper::toTeacherRatingCriteriaDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ratingDTOs);
    }

    @GetMapping("/teacher/{teacherId}/average")
    public ResponseEntity<Map<String, Object>> getAverageRatingsByTeacher(@PathVariable Integer teacherId) {
        Map<String, Object> averages = new HashMap<>();
        averages.put("subjectKnowledge", teacherRatingCriteriaService.getAverageSubjectKnowledgeByTeacherId(teacherId));
        averages.put("communicationSkills", teacherRatingCriteriaService.getAverageCommunicationSkillsByTeacherId(teacherId));
        averages.put("totalRatings", teacherRatingCriteriaService.getDistinctStudentCountByTeacherId(teacherId));
        return ResponseEntity.ok(averages);
    }

    @GetMapping
    public ResponseEntity<List<TeacherRatingCriteriaDTO>> getAllRatings() {
        List<TeacherRatingCriteria> ratings = teacherRatingCriteriaService.getAllRatings();
        List<TeacherRatingCriteriaDTO> ratingDTOs = ratings.stream()
                .map(DtoMapper::toTeacherRatingCriteriaDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ratingDTOs);
    }
}

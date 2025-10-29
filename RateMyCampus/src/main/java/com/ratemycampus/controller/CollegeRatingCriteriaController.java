package com.ratemycampus.controller;

import com.ratemycampus.entity.CollegeRatingCriteria;
import com.ratemycampus.dto.CollegeRatingCriteriaDTO;
import com.ratemycampus.dto.DtoMapper;
import com.ratemycampus.entity.Student;
import com.ratemycampus.service.CollegeRatingCriteriaService;
import com.ratemycampus.security.SecurityUtils;
import com.ratemycampus.service.StudentService;
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
@RequestMapping("/api/college-rating-criteria")
public class CollegeRatingCriteriaController {

    @Autowired
    private CollegeRatingCriteriaService collegeRatingCriteriaService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private StudentService studentService;

    @PostMapping
    public ResponseEntity<?> addRating(@Valid @RequestBody CollegeRatingCriteriaDTO ratingDTO, BindingResult result) {
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

            Student student= studentService.getStudentById( (long)ratingDTO.studentId);

            if( student.getCollege().getCid() != ratingDTO.collegeId)
            {
                HashMap<String, String> errors = new HashMap<>();
                errors.put("error", "You can only submit ratings for your College");
                return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
            }

            if( collegeRatingCriteriaService.CheckRating(ratingDTO.collegeId,(long)ratingDTO.studentId)!=null)
            {
                CollegeRatingCriteria existingrating=collegeRatingCriteriaService.CheckRating(ratingDTO.collegeId,(long)ratingDTO.studentId);

                existingrating.setCampusFacilities(ratingDTO.campusFacilities);
                existingrating.setExtracurricularActivities(ratingDTO.extracurricularActivities);
                existingrating.setSportsFacilities(ratingDTO.sportsFacilities);

                return new ResponseEntity<>(collegeRatingCriteriaService.addRating(existingrating), HttpStatus.BAD_REQUEST);


            }

            CollegeRatingCriteria rating = DtoMapper.toCollegeRatingCriteriaEntity(ratingDTO);
            CollegeRatingCriteria saved = collegeRatingCriteriaService.addRating(rating);
            CollegeRatingCriteriaDTO savedDTO = DtoMapper.toCollegeRatingCriteriaDTO(saved);
            return new ResponseEntity<>(savedDTO, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            HashMap<String, String> errors = new HashMap<>();
            errors.put("error", e.getMessage());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/college/{collegeId}")
    public ResponseEntity<List<CollegeRatingCriteriaDTO>> getRatingsByCollege(@PathVariable Long collegeId) {
        List<CollegeRatingCriteria> ratings = collegeRatingCriteriaService.getRatingsByCollege(collegeId);
        List<CollegeRatingCriteriaDTO> ratingDTOs = ratings.stream()
                .map(DtoMapper::toCollegeRatingCriteriaDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ratingDTOs);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<CollegeRatingCriteriaDTO>> getRatingsByStudent(@PathVariable Long studentId) {
        List<CollegeRatingCriteria> ratings = collegeRatingCriteriaService.getRatingsByStudent(studentId);
        List<CollegeRatingCriteriaDTO> ratingDTOs = ratings.stream()
                .map(DtoMapper::toCollegeRatingCriteriaDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ratingDTOs);
    }

    @GetMapping("/college/{collegeId}/average")
    public ResponseEntity<Map<String, Object>> getAverageRatingsByCollege(@PathVariable Long collegeId) {
        Map<String, Object> averages = new HashMap<>();
        averages.put("extracurricularActivities", collegeRatingCriteriaService.getAverageExtracurricularActivitiesByCollegeId(collegeId));
        averages.put("sportsFacilities", collegeRatingCriteriaService.getAverageSportsFacilitiesByCollegeId(collegeId));
        averages.put("campusFacilities", collegeRatingCriteriaService.getAverageCampusFacilitiesByCollegeId(collegeId));
        averages.put("totalRatings", collegeRatingCriteriaService.getDistinctStudentCountByCollegeId(collegeId));
        return ResponseEntity.ok(averages);
    }

    @GetMapping
    public ResponseEntity<List<CollegeRatingCriteriaDTO>> getAllRatings() {
        List<CollegeRatingCriteria> ratings = collegeRatingCriteriaService.getAllRatings();
        List<CollegeRatingCriteriaDTO> ratingDTOs = ratings.stream()
                .map(DtoMapper::toCollegeRatingCriteriaDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ratingDTOs);
    }
}

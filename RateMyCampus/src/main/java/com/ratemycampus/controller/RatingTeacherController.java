package com.ratemycampus.controller;

import com.ratemycampus.entity.Rating;
import com.ratemycampus.entity.RatingTeacher;
import com.ratemycampus.service.RatingTeacherService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/rating-teachers")
public class RatingTeacherController {

	@Autowired
	private RatingTeacherService service;

	@PostMapping
	public ResponseEntity<?> addRating(@Valid @RequestBody RatingTeacher rating, BindingResult result) {

		try {

			if (result.hasErrors()) {
				HashMap<String, String> errors = new HashMap<>();
				result.getFieldErrors().forEach(error -> {
					errors.put(error.getField(), error.getDefaultMessage());

				});

				return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
			}

			RatingTeacher saved = service.addRating(rating);
			return new ResponseEntity<>(saved, HttpStatus.CREATED);
		} catch (RuntimeException e) {
			HashMap<String, String> errors = new HashMap<>();
			errors.put("error", e.getMessage());
			return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
		}

	}

	@GetMapping
	public List<RatingTeacher> getAllRatings() {
		return service.getAllRatings();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Long id) {


        try {
            return ResponseEntity.ok(service.getRatingById(id));
        }
        catch (RuntimeException e)
        {
            HashMap<String, String> errors = new HashMap<>();
            errors.put("error",e.getMessage());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

	}

	@GetMapping("/teacher/{teacherId}")
	public List<RatingTeacher> getByTeacher(@PathVariable Long teacherId) {
		return service.getRatingsByTeacherId(teacherId);
	}

	@GetMapping("/student/{studentId}")
	public List<RatingTeacher> getByStudent(@PathVariable Long studentId) {
		return service.getRatingsByStudentId(studentId);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		service.deleteRating(id);
		return ResponseEntity.ok("Rating deleted successfully");
	}
	
	@PutMapping("/{id}")
    public ResponseEntity<?> updateRating(@PathVariable Long id, @RequestBody RatingTeacher rating) {
        try {
            RatingTeacher ratingTeacher=service.getRatingById(id);

            rating.setTeacher(ratingTeacher.getTeacher());
            ratingTeacher.setStudent(ratingTeacher.getStudent());
            return ResponseEntity.ok(service.updateRating(id, rating));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}

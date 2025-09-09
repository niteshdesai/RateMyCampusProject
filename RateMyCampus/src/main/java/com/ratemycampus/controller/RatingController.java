package com.ratemycampus.controller;

import com.ratemycampus.entity.Rating;
import com.ratemycampus.service.RatingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/ratings")
public class RatingController {

 
    @Autowired
    private RatingService ratingService;

   @GetMapping("/college/{collegeId}/student-count")
    public ResponseEntity<?> getDistinctStudentCountByCollegeId(@PathVariable Long collegeId) {
        long count = ratingService.getDistinctStudentCountByCollegeId(collegeId);
        return ResponseEntity.ok(count);
    }
    @GetMapping("/college/{collegeId}")
    public ResponseEntity<?> getAverageRatingByCollegeId(@PathVariable Long collegeId) {
        Double avg = ratingService.getAverageRatingByCollegeId(collegeId);
        if (avg == null) {
            return ResponseEntity.ok("No ratings found for this college");
        }
        return ResponseEntity.ok(avg);
    }

    @PostMapping
    public ResponseEntity<?> addRating(@Valid @RequestBody Rating rating,BindingResult result) {
    	 try {
         	
     		if (result.hasErrors()) {
     	        HashMap<String, String> errors = new HashMap<>();
     	        result.getFieldErrors().forEach(error -> {
     	            errors.put(error.getField(), error.getDefaultMessage());
     	
     	        });
     	
     	        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
     	 }
     	
         Rating saved = ratingService.addRating(rating);
         return new ResponseEntity<>(saved, HttpStatus.CREATED);
     } catch (RuntimeException e) {
     	HashMap<String, String> errors = new HashMap<>();
         errors.put("error", e.getMessage());
         return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
     }
    }

    @GetMapping
    public List<Rating> getAllRatings() {
        return ratingService.getAllRatings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRatingById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ratingService.getRatingById(id));
        } catch (Exception ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRating(@PathVariable Long id) {
        try {
            ratingService.deleteRating(id);
            return ResponseEntity.ok("Rating deleted successfully");
        } catch (Exception ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRating(@PathVariable Long id, @RequestBody Rating rating) {
        try {
            return ResponseEntity.ok(ratingService.updateRating(id, rating));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}

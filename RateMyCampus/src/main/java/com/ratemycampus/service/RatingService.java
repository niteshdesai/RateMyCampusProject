package com.ratemycampus.service;

import com.ratemycampus.entity.College;
import com.ratemycampus.entity.Rating;
import com.ratemycampus.entity.Student;
import com.ratemycampus.repository.CollegeRepository;
import com.ratemycampus.repository.RatingRepository;
import com.ratemycampus.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService {
    public Double getAverageRatingByCollegeId(Long collegeId) {
        List<Rating> ratings = ratingRepo.findByCollegeCid(collegeId);
        if (ratings.isEmpty()) {
            return null;
        }
        double sum = 0;
        for (Rating rating : ratings) {
            sum += rating.getScore();
        }
        return sum / ratings.size();
    }
    public List<Rating> getRatingsByCollegeId(Long collegeId) {
        return ratingRepo.findByCollegeCid(collegeId);
    }

    @Autowired
    private RatingRepository ratingRepo;

    @Autowired
    private CollegeRepository collegeRepo;

    @Autowired
    private StudentRepository studentRepo;

    public Rating addRating(Rating rating) {
        if (rating.getCollege() == null || rating.getStudent() == null) {
            throw new IllegalArgumentException("College and Student must not be null");
        }

        Long collegeId = rating.getCollege().getCid();
        Integer studentId = rating.getStudent().getSid();

        College college = collegeRepo.findById(collegeId)
                .orElseThrow(() -> new EntityNotFoundException("College not found with ID: " + collegeId));

        Student student = studentRepo.findBySid(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + studentId));

        // Prevent duplicate rating by same student for same college
        if (ratingRepo.existsByCollege_CidAndStudent_Sid(collegeId, studentId)) {

            Rating rating1=ratingRepo.findByCollege_CidAndStudent_Sid(collegeId,studentId);
           
            return updateRating(rating1.getId(),rating);

        }

        rating.setCollege(college);
        rating.setStudent(student);
        return ratingRepo.save(rating);
    }

    public List<Rating> getAllRatings() {
        return ratingRepo.findAll();
    }

    public Rating getRatingById(Long id) {
        return ratingRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rating not found with ID: " + id));
    }

    public void deleteRating(Long id) {
        if (!ratingRepo.existsById(id)) {
            throw new EntityNotFoundException("Rating not found with ID: " + id);
        }
        ratingRepo.deleteById(id);
    }

    public Rating updateRating(Long id, Rating updatedRating) {
        Rating existing = getRatingById(id);
        existing.setScore(updatedRating.getScore());
        return ratingRepo.save(existing);
    }
}

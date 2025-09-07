package com.ratemycampus.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ratemycampus.entity.College;
import com.ratemycampus.service.CollegeService;

import jakarta.validation.Valid;

@RestController

@RequestMapping("/api/colleges")
@CrossOrigin
public class CollegeController {

	@Autowired
	private CollegeService collegeService;

	@GetMapping
	public ResponseEntity<?> getAllColleges() {
		List<College> colleges = collegeService.getAllColleges();
		return ResponseEntity.ok(colleges);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getCollegeById(@PathVariable Long id) {
		College college = collegeService.getCollegeById(id);
		return ResponseEntity.ok(college);
	}

	@PostMapping(value = "/addcollege", consumes = { "multipart/form-data" })
	public ResponseEntity<?> createCollege(@Valid @RequestPart College college, BindingResult result,
			@RequestPart(value = "image", required = false) MultipartFile image) {
		try {
			if (result.hasErrors()) {
				HashMap<String, String> errors = new HashMap<>();
				result.getFieldErrors().forEach(error -> {
					errors.put(error.getField(), error.getDefaultMessage());
				});
				return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
			}

			if (image != null && !image.isEmpty()) {
				if (!isImageFile(image)) {
					HashMap<String, String> errors = new HashMap<>();
					errors.put("image", "File must be an image (jpg, jpeg, png)");
					return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
				}
				String imagePath = saveImage(image);

				college.setCimg(imagePath);
			}

			College saved = collegeService.createCollege(college);
			return new ResponseEntity<>(saved, HttpStatus.CREATED);
		} catch (RuntimeException | IOException e) {
			HashMap<String, String> errors = new HashMap<>();
			errors.put("error", e.getMessage());
			return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateCollege(@Valid @RequestBody College college,BindingResult result, @PathVariable Long id
                                           ) {
		try {
			if (result.hasErrors()) {
				Map<String, String> errors = new HashMap<>();
				result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
				return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
			}

			college.setCimg(collegeService.getCollegeById(id).getCimg());
			College updated = collegeService.updateCollege(id, college);

			return ResponseEntity.ok(updated);
		} catch (Exception e) {
			HashMap<String, String> errors = new HashMap<>();
			errors.put("error", "Failed to Update college: " + e.getMessage());
			return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteCollege(@PathVariable Long id) {
		collegeService.deleteCollege(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/search")
	public ResponseEntity<?> searchColleges(@RequestParam String name) {
		return ResponseEntity.ok(collegeService.searchCollegesByName(name));
	}

	@GetMapping("/{id}/departments")
	public ResponseEntity<?> getDepartmentsByCollege(@PathVariable Long id) {
		return ResponseEntity.ok(collegeService.getDepartmentsByCollege(id));
	}

	@GetMapping("/{id}/ratings")
	public ResponseEntity<?> getRatingsByCollege(@PathVariable Long id) {
		return ResponseEntity.ok(collegeService.getRatingsByCollege(id));
	}

	@GetMapping("/city/{city}")
	public ResponseEntity<?> getCollegesByCity(@PathVariable String city) {
		return ResponseEntity.ok(collegeService.getCollegesByCity(city));
	}

	@GetMapping("/{id}/rating-count")
	public ResponseEntity<?> getRatingCountByCollege(@PathVariable Long id) {
		long studentCount = collegeService.getRatingCountByCollegeId(id);
		return ResponseEntity.ok(studentCount);
	}

	@GetMapping("/{Id}/student-count")
	public ResponseEntity<?> getStudentCount(@PathVariable Long Id) {
		long count = collegeService.getStudentCountByCollegeId(Id);
		return ResponseEntity.ok(count);
	}

	@PutMapping(value = "/updateCollegeImage/{Id}", consumes = { "multipart/form-data" })
	public ResponseEntity<?> updateCollegeImage(@PathVariable Long Id,
			@RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
		College college = collegeService.getCollegeById(Id);
		String prevImage = college.getCimg();
		try {
			if (image != null && !image.isEmpty()) {
				if (!isImageFile(image)) {
					HashMap<String, String> errors = new HashMap<>();
					errors.put("image", "File must be an image (jpg, jpeg, png)");
					return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
				}
				String uploadDir = new ClassPathResource("static/image").getFile().getAbsolutePath();
				Path uploadPath = Paths.get(uploadDir).resolve(prevImage);
				File imageFiles = uploadPath.toFile();
				
				if(imageFiles.exists())
				{
					imageFiles.delete();
				}
				
				String imagePath = saveImage(image);
				college.setCimg(imagePath);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		College updated = collegeService.updateCollege(Id, college);
		return ResponseEntity.ok(updated);
	}

	private boolean isImageFile(MultipartFile file) {
		String contentType = file.getContentType();
		return contentType != null && (contentType.equals("image/jpeg") || contentType.equals("image/png")
				|| contentType.equals("image/jpg"));
	}

	private String saveImage(MultipartFile image) throws IOException {
		Path filePath = null;
		String fileName = "";
		try {
			String uploadDir = new ClassPathResource("static/image").getFile().getAbsolutePath();
			Path uploadPath = Paths.get(uploadDir);

			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			fileName = image.getOriginalFilename();

			filePath = uploadPath.resolve(fileName);

			Files.copy(image.getInputStream(), filePath);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return fileName;
	}

}
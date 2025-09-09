package com.ratemycampus.controller;

import com.ratemycampus.entity.Student;
import com.ratemycampus.entity.Teacher;
import com.ratemycampus.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/students")

public class StudentController {

    @Autowired
    private StudentService studentService;
    
    @Autowired
    private CollegeController collegeController;
    
    @GetMapping
    public ResponseEntity<List<Student>> getAll() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(studentService.getStudentById(id));
        } catch (RuntimeException e) {
            HashMap<String, String> errors = new HashMap<>();
            errors.put("error",e.getMessage());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/enroll/{enrollmentNumber}")
    public ResponseEntity<?> getByEnrollment(@PathVariable String enrollmentNumber) {

        try {
            return ResponseEntity.ok(studentService.getByEnrollmentNumber(enrollmentNumber));
        } catch (RuntimeException e) {
            HashMap<String, String> errors = new HashMap<>();
            errors.put("error",e.getMessage());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping( value = "/addStudent" ,consumes = {"multipart/form-data"})
    public ResponseEntity<?> create(@Valid @RequestPart Student student,
    		BindingResult result,
    		 @RequestPart(value = "image", required = false) MultipartFile image
    		) throws IOException {
       
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
               student.setSimg(imagePath);
           }
          Student saved = studentService.createStudent(student);
          return new ResponseEntity<>(saved, HttpStatus.CREATED);
      } catch (RuntimeException e) {
      	HashMap<String, String> errors = new HashMap<>();
          errors.put("error", e.getMessage());
          return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
      }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Student student,BindingResult result , @PathVariable Long id) {
        
    	Student oldStudent = studentService.getStudentById(id);
		student.setSimg(oldStudent.getSimg());
	
    	
    	try {
			if (result.hasErrors()) {
				Map<String, String> errors = new HashMap<>();
				result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
				return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
			}
			
			Student updatedData = studentService.updateStudent(id, student);
			
			return ResponseEntity.ok(updatedData);
		} catch (Exception e) {
			HashMap<String, String> errors = new HashMap<>();
			errors.put("error", "Failed to Update college: " + e.getMessage());
			return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
		}
    	
    }
    @PutMapping(value = "/updateStudentImage/{Id}", consumes = { "multipart/form-data" })
   	public ResponseEntity<?> updateStudentImage(@PathVariable Long Id,
   			@RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
           Student oldStudent = studentService.getStudentById(Id);
           String prevImage = oldStudent.getSimg();
           try {
               if (image != null && !image.isEmpty()) {
                   if (!isImageFile(image)) {
                       HashMap<String, String> errors = new HashMap<>();
                       errors.put("image", "File must be an image (jpg, jpeg, png)");
                       return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
                   }
                   if (prevImage != null && !prevImage.isEmpty()) {
                       File oldFile = new File(prevImage);
                       if (oldFile.exists()) {
                           oldFile.delete();
                       }
                   }
                   String imagePath = saveImage(image);
                   oldStudent.setSimg(imagePath);
               }
           } catch (Exception e) {
               HashMap<String, String> errors = new HashMap<>();
               errors.put("error", e.getMessage());
               return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
           }

           Student updated = studentService.updateStudent(Id, oldStudent);
           return ResponseEntity.ok(updated);
   	}
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/college/{collegeId}")
    public ResponseEntity<List<Student>> getStudentsByCollege(@PathVariable Long collegeId) {
        return ResponseEntity.ok(studentService.getStudentsByCollege(collegeId));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<Student>> getStudentsByDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(studentService.getStudentsByDepartment(departmentId));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Student>> getStudentsByCourse(@PathVariable Integer courseId) {
        return ResponseEntity.ok(studentService.getStudentsByCourse(courseId));
    }
    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (
            contentType.equals("image/jpeg") ||
            contentType.equals("image/png") ||
            contentType.equals("image/jpg")
        );
    }

    private String saveImage(MultipartFile image) throws IOException {
            String uploadDir = "uploads/student-images/";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String originalFileName = image.getOriginalFilename();
            String fileExtension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            String fileName = java.util.UUID.randomUUID() + fileExtension;
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(image.getInputStream(), filePath);
            return uploadDir + fileName;
    }

}


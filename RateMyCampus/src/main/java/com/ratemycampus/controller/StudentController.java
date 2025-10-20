package com.ratemycampus.controller;

import com.ratemycampus.entity.Student;
import com.ratemycampus.dto.StudentDTO;
import com.ratemycampus.dto.DtoMapper;
import com.ratemycampus.service.StudentService;
import com.ratemycampus.security.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/students")

public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private SecurityUtils securityUtils;
    
    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAll() {
        List<Student> students = studentService.getAllStudents();
        List<StudentDTO> studentDTOs = students.stream()
                .map(DtoMapper::toStudentDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(studentDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            Student student = studentService.getStudentById(id);
            StudentDTO studentDTO = DtoMapper.toStudentDTO(student);
            return ResponseEntity.ok(studentDTO);
        } catch (RuntimeException e) {
            HashMap<String, String> errors = new HashMap<>();
            errors.put("error",e.getMessage());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/enroll/{enrollmentNumber}")
    public ResponseEntity<?> getByEnrollment(@PathVariable String enrollmentNumber) {
        try {
            Student student = studentService.getByEnrollmentNumber(enrollmentNumber);
            StudentDTO studentDTO = DtoMapper.toStudentDTO(student);
            return ResponseEntity.ok(studentDTO);
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
      	 
      	 // Validate department ownership for HOD role
      	 String currentUserRole = securityUtils.getCurrentUserRole();
      	 if ("ROLE_HOD".equals(currentUserRole)) {
      	     Long currentUserDepartmentId = securityUtils.getCurrentUserDepartmentId();
      	     if (currentUserDepartmentId == null || !currentUserDepartmentId.equals(student.getDepartment().getDeptId())) {
      	         HashMap<String, String> errors = new HashMap<>();
      	         errors.put("error", "You can only add students to your own department");
      	         return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
      	     }
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
        
    	try {
    		Student oldStudent = studentService.getStudentById(id);
    		
    		// Validate department ownership for HOD role
    		String currentUserRole = securityUtils.getCurrentUserRole();
    		if ("ROLE_HOD".equals(currentUserRole)) {
    		    Long currentUserDepartmentId = securityUtils.getCurrentUserDepartmentId();
    		    if (currentUserDepartmentId == null || !currentUserDepartmentId.equals(oldStudent.getDepartment().getDeptId())) {
    		        HashMap<String, String> errors = new HashMap<>();
    		        errors.put("error", "You can only update students that belong to your department");
    		        return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
    		    }
    		    
    		    // Ensure the updated student also belongs to the same department
    		    if (!currentUserDepartmentId.equals(student.getDepartment().getDeptId())) {
    		        HashMap<String, String> errors = new HashMap<>();
    		        errors.put("error", "You cannot change the department of a student");
    		        return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
    		    }
    		}
    		
    		student.setSimg(oldStudent.getSimg());
    		
			if (result.hasErrors()) {
				Map<String, String> errors = new HashMap<>();
				result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
				return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
			}
			
			Student updatedData = studentService.updateStudent(id, student);
			
			return ResponseEntity.ok(updatedData);
		} catch (Exception e) {
			HashMap<String, String> errors = new HashMap<>();
			errors.put("error", "Failed to Update student: " + e.getMessage());
			return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
		}
    }
    @PutMapping(value = "/updateStudentImage/{Id}", consumes = { "multipart/form-data" })
   	public ResponseEntity<?> updateStudentImage(@PathVariable Long Id,
   			@RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
           try {
               Student oldStudent = studentService.getStudentById(Id);
               
               // Validate department ownership for HOD role
               String currentUserRole = securityUtils.getCurrentUserRole();
               if ("ROLE_HOD".equals(currentUserRole)) {
                   Long currentUserDepartmentId = securityUtils.getCurrentUserDepartmentId();
                   if (currentUserDepartmentId == null || !currentUserDepartmentId.equals(oldStudent.getDepartment().getDeptId())) {
                       HashMap<String, String> errors = new HashMap<>();
                       errors.put("error", "You can only update student images that belong to your department");
                       return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
                   }
               }
               
               String prevImage = oldStudent.getSimg();
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
               
               Student updated = studentService.updateStudent(Id, oldStudent);
               return ResponseEntity.ok(updated);
           } catch (Exception e) {
               HashMap<String, String> errors = new HashMap<>();
               errors.put("error", e.getMessage());
               return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
           }
   	}
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            // Validate department ownership for HOD role
            String currentUserRole = securityUtils.getCurrentUserRole();
            if ("ROLE_HOD".equals(currentUserRole)) {
                Student existingStudent = studentService.getStudentById(id);
                Long currentUserDepartmentId = securityUtils.getCurrentUserDepartmentId();
                if (currentUserDepartmentId == null || !currentUserDepartmentId.equals(existingStudent.getDepartment().getDeptId())) {
                    HashMap<String, String> errors = new HashMap<>();
                    errors.put("error", "You can only delete students that belong to your department");
                    return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
                }
            }
            
            studentService.deleteStudent(id);
            return ResponseEntity.ok("Student deleted successfully");
        } catch (Exception e) {
            HashMap<String, String> errors = new HashMap<>();
            errors.put("error", e.getMessage());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/college/{collegeId}")
    public ResponseEntity<List<StudentDTO>> getStudentsByCollege(@PathVariable Long collegeId) {
        List<Student> students = studentService.getStudentsByCollege(collegeId);
        List<StudentDTO> studentDTOs = students.stream()
                .map(DtoMapper::toStudentDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(studentDTOs);
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<StudentDTO>> getStudentsByDepartment(@PathVariable Long departmentId) {
        List<Student> students = studentService.getStudentsByDepartment(departmentId);
        List<StudentDTO> studentDTOs = students.stream()
                .map(DtoMapper::toStudentDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(studentDTOs);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<StudentDTO>> getStudentsByCourse(@PathVariable Integer courseId) {
        List<Student> students = studentService.getStudentsByCourse(courseId);
        List<StudentDTO> studentDTOs = students.stream()
                .map(DtoMapper::toStudentDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(studentDTOs);
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
        // Return relative path for storage in DB
        return uploadDir + fileName;
    }

}


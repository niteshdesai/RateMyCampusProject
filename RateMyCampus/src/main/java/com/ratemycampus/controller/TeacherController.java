package com.ratemycampus.controller;


import com.ratemycampus.entity.College;
import com.ratemycampus.entity.Course;
import com.ratemycampus.entity.Student;
import com.ratemycampus.entity.Teacher;
import com.ratemycampus.service.TeacherService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
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
@RequestMapping("/api/teachers")
//@CrossOrigin("*")
public class TeacherController {

    @Autowired private TeacherService teacherService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> addTeacher(@Valid @RequestPart Teacher teacher,
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
         
               teacher.setTimg(imagePath);
           }
     	
     		Teacher saved = teacherService.addTeacher(teacher);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
     } catch (EntityNotFoundException e) {
     	HashMap<String, String> errors = new HashMap<>();
         errors.put("error", e.getMessage());
         return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
     }
    }

    @GetMapping
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        return ResponseEntity.ok(teacherService.getAllTeachers());
    }

    @GetMapping("/college/{collegeId}")
    public ResponseEntity<List<Teacher>> getTeachersByCollege(@PathVariable Long collegeId) {
        return ResponseEntity.ok(teacherService.getTeachersByCollege(collegeId));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<Teacher>> getTeachersByDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(teacherService.getTeachersByDepartment(departmentId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable Integer id) {
        return ResponseEntity.ok(teacherService.getTeacherById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTeacher(@Valid @RequestBody Teacher updated,BindingResult result,  @PathVariable Integer id) {
    	Teacher teacher = teacherService.getTeacherById(id);
		updated.setTimg(teacher.getTimg());
        
    	
    	try {
			if (result.hasErrors()) {
				Map<String, String> errors = new HashMap<>();
				result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
				return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
			}
			
			Teacher updatedData = teacherService.updateTeacher(id, updated);
			
			return ResponseEntity.ok(updatedData);
		} catch (Exception e) {
			HashMap<String, String> errors = new HashMap<>();
			errors.put("error", "Failed to Update college: " + e.getMessage());
			return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
		}
    	
    	
    }
    @PutMapping(value = "/updateTeacherImage/{Id}", consumes = { "multipart/form-data" })
	public ResponseEntity<?> updateTeacherImage(@PathVariable Integer Id,
			@RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
		Teacher teacher = teacherService.getTeacherById(Id);
		String prevImage = teacher.getTimg();
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
				teacher.setTimg(imagePath);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		Teacher updated = teacherService.updateTeacher(Id, teacher);
		return ResponseEntity.ok(updated);
	}

    // New endpoints for managing teacher courses via TeacherCourse join table could be added
    // in a dedicated controller or here under /api/teachers/{teacherId}/courses
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTeacher(@PathVariable Integer id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.ok("Teacher deleted successfully");
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
    	Path filePath=null;
    	String fileName="";
    	try {
    	String uploadDir =new ClassPathResource("static/image").getFile().getAbsolutePath();
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

package com.ratemycampus.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import com.ratemycampus.service.DepartmentService;

import com.ratemycampus.exception.ResourceNotFoundException;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ratemycampus.entity.College;
import com.ratemycampus.entity.DepartmentAdmin;
import com.ratemycampus.service.DepartmentAdminService;

@RestController
@RequestMapping("/api/hod")
public class DepartmentAdminController {

//    private final DepartmentService departmentService;

    @Autowired
    private DepartmentAdminService service;



    // ✅ Create
     @PostMapping(consumes = { "multipart/form-data" })
 	public ResponseEntity<?> createDeptAdmin(@Valid @RequestPart DepartmentAdmin admin, BindingResult result,
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

 				admin.setDaImg(imagePath);
 			}
             else {
                HashMap<String, String> errors = new HashMap<>();
                errors.put("image", "Image Is Required");
                return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
            }

 			DepartmentAdmin saved=service.createHod(admin);
 			return new ResponseEntity<>(saved, HttpStatus.CREATED);
 		} catch (RuntimeException | IOException  e) {
 			HashMap<String, String> errors = new HashMap<>();
 			errors.put("error", e.getMessage());
 			return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
 		}
 	}
    // ✅ Get All
    @GetMapping
    public List<DepartmentAdmin> getAllDeptAdmins() {
        return service.getAllHods();

    }

    // ✅ Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getDeptAdminById(@PathVariable Integer id) {
      try {
          return ResponseEntity.ok(service.getHodById(id));
      }catch (RuntimeException e)
      {
          HashMap<String, String> errors = new HashMap<>();
          errors.put("error", e.getMessage());
          return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
      }

    }

    // ✅ Update
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDeptAdmin(@PathVariable Integer id, @Valid @RequestBody DepartmentAdmin updatedAdmin,BindingResult result) {
    	
    	try {
    	if (result.hasErrors()) {
				HashMap<String, String> errors = new HashMap<>();
				result.getFieldErrors().forEach(error -> {
					errors.put(error.getField(), error.getDefaultMessage());
				});
				return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
			}
        updatedAdmin.setDaImg(service.getHodById(id).getDaImg());
        return ResponseEntity.ok(service.updateHod(id, updatedAdmin));
    	}
    	catch (Exception e) {
			HashMap<String, String> errors = new HashMap<>();
			errors.put("error", "Failed to Update Department Admin: " + e.getMessage());
			return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
		}
    	
    	
    	
    }
    
    @PutMapping(value = "/updateDeptAdminImage/{Id}", consumes = { "multipart/form-data" })
	public ResponseEntity<?> updateDeptAdminImage(@PathVariable Integer Id,
			@RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
		DepartmentAdmin deptAdmin = service.getHodById(Id);
		String prevImage = deptAdmin.getDaImg();

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
				deptAdmin.setDaImg(imagePath);
			}
			DepartmentAdmin updated = service.updateHod(Id, deptAdmin);
			return ResponseEntity.ok(updated);

		} catch (Exception e) {
			HashMap<String, String> errors = new HashMap<>();
			errors.put("error", "Failed to Update Department Admin Image: " + e.getMessage());
			return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
		}
    	
		
	}

    // ✅ Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDeptAdmin(@PathVariable Integer id) {
    	service.deleteHodById(id);
        return ResponseEntity.ok("Department Admin deleted successfully");
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


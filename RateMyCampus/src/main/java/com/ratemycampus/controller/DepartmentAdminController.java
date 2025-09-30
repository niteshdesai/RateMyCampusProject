package com.ratemycampus.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ratemycampus.entity.DepartmentAdmin;
import com.ratemycampus.service.DepartmentAdminService;
import com.ratemycampus.security.SecurityUtils;

@RestController
@CrossOrigin
@RequestMapping("/api/hod")
public class DepartmentAdminController {

//    private final DepartmentService departmentService;

    @Autowired
    private DepartmentAdminService service;

    @Autowired
    private SecurityUtils securityUtils;



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
			
			// Validate college ownership
			Long currentUserCollegeId = securityUtils.getCurrentUserCollegeId();
			if (currentUserCollegeId == null || !currentUserCollegeId.equals(admin.getCollege().getCid())) {
				HashMap<String, String> errors = new HashMap<>();
				errors.put("error", "You can only create HODs for your own college");
				return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
			}
			
			if (image != null && !image.isEmpty()) {
				if (!isImageFile(image)) {
					HashMap<String, String> errors = new HashMap<>();
					errors.put("image", "File must be an image (jpg, jpeg, png)");
					return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
				}
				String imagePath = saveImage(image);
				admin.setDaImg(imagePath);
			} else {
				HashMap<String, String> errors = new HashMap<>();
				errors.put("image", "Image Is Required");
				return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
			}
			DepartmentAdmin saved = service.createHod(admin);
			return new ResponseEntity<>(saved, HttpStatus.CREATED);
		} catch (RuntimeException | IOException e) {
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
			
		// Validate college ownership
		Long currentUserCollegeId = securityUtils.getCurrentUserCollegeId();
		DepartmentAdmin existingAdmin = service.getHodById(id);
		
		if (currentUserCollegeId == null || !currentUserCollegeId.equals(existingAdmin.getCollege().getCid())) {
			HashMap<String, String> errors = new HashMap<>();
			errors.put("error", "You can only update HODs that belong to your college");
			return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
		}
		
		// Ensure the updated admin also belongs to the same college
		if (!currentUserCollegeId.equals(updatedAdmin.getCollege().getCid())) {
			HashMap<String, String> errors = new HashMap<>();
			errors.put("error", "You cannot change the college of a HOD");
			return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
		}
		
        updatedAdmin.setDaImg(existingAdmin.getDaImg());
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
		try {
			// Validate college ownership
			Long currentUserCollegeId = securityUtils.getCurrentUserCollegeId();
			DepartmentAdmin deptAdmin = service.getHodById(Id);
			
			if (currentUserCollegeId == null || !currentUserCollegeId.equals(deptAdmin.getCollege().getCid())) {
				HashMap<String, String> errors = new HashMap<>();
				errors.put("error", "You can only update HOD images that belong to your college");
				return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
			}
			
			String prevImage = deptAdmin.getDaImg();
			if (image != null && !image.isEmpty()) {
				if (!isImageFile(image)) {
					HashMap<String, String> errors = new HashMap<>();
					errors.put("image", "File must be an image (jpg, jpeg, png)");
					return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
				}
				// Delete old image if exists
				if (prevImage != null && !prevImage.isEmpty()) {
					File oldFile = new File("uploads/department-admin-images/" + prevImage);
					if (oldFile.exists()) {
						oldFile.delete();
					}
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
    	try {
    		// Validate college ownership
    		Long currentUserCollegeId = securityUtils.getCurrentUserCollegeId();
    		DepartmentAdmin existingAdmin = service.getHodById(id);
    		
    		if (currentUserCollegeId == null || !currentUserCollegeId.equals(existingAdmin.getCollege().getCid())) {
    			HashMap<String, String> errors = new HashMap<>();
    			errors.put("error", "You can only delete HODs that belong to your college");
    			return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
    		}
    		
    		service.deleteHodById(id);
    		return ResponseEntity.ok("Department Admin deleted successfully");
    	} catch (Exception e) {
    		HashMap<String, String> errors = new HashMap<>();
    		errors.put("error", e.getMessage());
    		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    	}
    }
    
    // ✅ Get by Department ID
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<?> getDeptAdminByDepartmentId(@PathVariable Long departmentId) {
        DepartmentAdmin admin = service.getHodByDepartmentId(departmentId);


        if (admin != null) {
            return ResponseEntity.ok(admin);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Department Admin not found for department ID: " + departmentId);
        }
    }
    
    private boolean isImageFile(MultipartFile file) {
		String contentType = file.getContentType();
		return contentType != null && (contentType.equals("image/jpeg") || contentType.equals("image/png")
				|| contentType.equals("image/jpg"));
	}

    private String saveImage(MultipartFile image) throws IOException {
        String uploadDir = "uploads/Department-admin-images/";
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


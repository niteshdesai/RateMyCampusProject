
package com.ratemycampus.controller;


import com.ratemycampus.entity.CollegeAdmin;
import com.ratemycampus.service.CollegeAdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
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
@RequestMapping("/api/college-admin")
@CrossOrigin
public class CollegeAdminController {

    @Autowired
    private CollegeAdminService service;

   
    @PostMapping(value = "/addcollegeAdmin", consumes = { "multipart/form-data" })
	public ResponseEntity<?> createCollege(@Valid @RequestPart CollegeAdmin collegeAdmin, BindingResult result,
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

				collegeAdmin.setImagePath(imagePath);
			}

			CollegeAdmin saved = service.addCollegeAdmin(collegeAdmin);
			return new ResponseEntity<>(saved, HttpStatus.CREATED);
		} catch (RuntimeException | IOException  e) {
			HashMap<String, String> errors = new HashMap<>();
			errors.put("error", e.getMessage());
			return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
		}
	}
    @PutMapping("/{id}")
	public ResponseEntity<?> updateCollegeAdmin(@Valid @RequestBody CollegeAdmin collegeAdmin,BindingResult result, @PathVariable Long id
			) {
		try {
			if (result.hasErrors()) {
				Map<String, String> errors = new HashMap<>();
				result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
				return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
			}

			collegeAdmin.setImagePath(service.getCollegeAdmin(id).getImagePath());
			CollegeAdmin updated = service.updateCollegeAdmin(id, collegeAdmin);

			return ResponseEntity.ok(updated);
		} catch (Exception e) {
			HashMap<String, String> errors = new HashMap<>();
			errors.put("error", "Failed to Update college: " + e.getMessage());
			return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
		}
	}
    @PutMapping(value = "/updateCollegeAdminImage/{Id}", consumes = { "multipart/form-data" })
	public ResponseEntity<?> updateCollegeAdminImage(@PathVariable Long Id,
			@RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
		CollegeAdmin collegeAdmin = service.getCollegeAdmin(Id);
		String prevImage = collegeAdmin.getImagePath();
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
				collegeAdmin.setImagePath(imagePath);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		CollegeAdmin updated = service.updateCollegeAdmin(Id, collegeAdmin);
		return ResponseEntity.ok(updated);
	}

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        service.deleteCollegeAdmin(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CollegeAdmin> getAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(service.getCollegeAdmin(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<CollegeAdmin>> getAllAdmins() {
        return ResponseEntity.ok(service.getAllCollegeAdmins());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        try {
            CollegeAdmin admin = service.login(email, password);
            return ResponseEntity.ok(admin);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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

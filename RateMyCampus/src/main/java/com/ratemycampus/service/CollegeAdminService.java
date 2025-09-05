package com.ratemycampus.service;

import com.ratemycampus.entity.CollegeAdmin;
import com.ratemycampus.repository.CollegeAdminRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class CollegeAdminService {

    @Autowired
    public CollegeAdminRepository adminRepository;


    public CollegeAdmin addCollegeAdmin(CollegeAdmin collegeAdmin) {
        if (adminRepository.existsByEmail(collegeAdmin.getEmail())) {
            throw new RuntimeException("College admin with this email already exists");
        }
        return adminRepository.save(collegeAdmin);
    }


    public CollegeAdmin updateCollegeAdmin(Long id, CollegeAdmin updatedAdmin) {
        CollegeAdmin existing = getCollegeAdmin(id);
        existing.setName(updatedAdmin.getName());
        existing.setEmail(updatedAdmin.getEmail());
        existing.setPassword(updatedAdmin.getPassword());
        existing.setMobile(updatedAdmin.getMobile());
        existing.setCollege(updatedAdmin.getCollege());
        existing.setImagePath(existing.getImagePath());
        return adminRepository.save(existing);
    }

 
    public void deleteCollegeAdmin(Long id) {
        CollegeAdmin admin = getCollegeAdmin(id);
        // Optional: delete image file
        if (admin.getImagePath() != null) {
            try {
                Path path = Paths.get("src/main/resources/static/image", admin.getImagePath());
                Files.deleteIfExists(path);
            } catch (Exception e) {
                throw new RuntimeException("Failed to delete admin image: " + e.getMessage());
            }
        }
        adminRepository.delete(admin);
    }

  
//    public CollegeAdmin getCollegeAdmin(Long id) {
//        return adminRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Admin not found with id: " + id));
//    }


    public List<CollegeAdmin> getAllCollegeAdmins() {
        return adminRepository.findAll();
    }


    public CollegeAdmin login(String email, String password) {
        CollegeAdmin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Invalid email"));

        if (!admin.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        return admin;
    }

    // ✅ Optional: Save image (same logic from controller)
    public String saveImage(MultipartFile image) {
        try {
            String uploadDir = new ClassPathResource("static/image").getFile().getAbsolutePath();
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(image.getInputStream(), filePath);
            return fileName;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save image: " + e.getMessage());
        }
    }

    // ✅ Optional: Validate image file
    public boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (
                contentType.equals("image/jpeg") ||
                contentType.equals("image/png") ||
                contentType.equals("image/jpg")
        );
    }


	public CollegeAdmin getCollegeAdmin(Long id) {
		return adminRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Admin not found with id: " + id));
	}
}

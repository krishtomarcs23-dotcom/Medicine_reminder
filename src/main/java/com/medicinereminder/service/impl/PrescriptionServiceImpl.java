package com.medicinereminder.service.impl;

import com.medicinereminder.entity.Prescription;
import com.medicinereminder.entity.User;
import com.medicinereminder.repository.PrescriptionRepository;
import com.medicinereminder.service.PrescriptionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {
    
    private final PrescriptionRepository prescriptionRepository;
    
    public PrescriptionServiceImpl(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }
    
    private static final String UPLOAD_DIR = "uploads/prescriptions";
    
    @Override
    @Transactional
    public Prescription uploadPrescription(MultipartFile file, User user) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        String originalFileName = file.getOriginalFilename();
        String extension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        
        String fileName = UUID.randomUUID().toString() + extension;
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        
        Prescription prescription = Prescription.builder()
                .fileName(fileName)
                .originalFileName(originalFileName)
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .user(user)
                .build();
        
        return prescriptionRepository.save(prescription);
    }
    
    @Override
    @Transactional
    public void deletePrescription(Long id, User user) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));
        
        if (!prescription.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        
        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(prescription.getFileName());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            // Log error but continue with database deletion
        }
        
        prescriptionRepository.delete(prescription);
    }
    
    @Override
    public List<Prescription> findByUser(User user) {
        return prescriptionRepository.findByUserOrderByUploadDateDesc(user);
    }
    
    @Override
    public Prescription findById(Long id) {
        return prescriptionRepository.findById(id).orElse(null);
    }
    
    @Override
    public byte[] getFileData(Long id) throws IOException {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));
        
        Path filePath = Paths.get(UPLOAD_DIR).resolve(prescription.getFileName());
        return Files.readAllBytes(filePath);
    }
    
    @Override
    public long count() {
        return prescriptionRepository.count();
    }
    
    @Override
    public List<Prescription> findAll() {
        return prescriptionRepository.findAll();
    }
}

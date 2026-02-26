package com.medicinereminder.service;

import com.medicinereminder.entity.Prescription;
import com.medicinereminder.entity.User;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface PrescriptionService {
    Prescription uploadPrescription(MultipartFile file, User user) throws IOException;
    void deletePrescription(Long id, User user);
    List<Prescription> findByUser(User user);
    Prescription findById(Long id);
    byte[] getFileData(Long id) throws IOException;
    long count();
    List<Prescription> findAll();
}

package com.medicinereminder.service;

import com.medicinereminder.dto.MedicineDTO;
import com.medicinereminder.entity.Medicine;
import com.medicinereminder.entity.User;
import java.time.LocalDate;
import java.util.List;

public interface MedicineService {
    Medicine createMedicine(MedicineDTO dto, User user);
    Medicine updateMedicine(Long id, MedicineDTO dto, User user);
    void deleteMedicine(Long id, User user);
    List<Medicine> findByUser(User user);
    List<Medicine> findActiveMedicines(User user);
    List<Medicine> findTodayMedicines(User user);
    Medicine findById(Long id);
    long countActiveByUser(User user);
    List<Medicine> findAllActive();
}

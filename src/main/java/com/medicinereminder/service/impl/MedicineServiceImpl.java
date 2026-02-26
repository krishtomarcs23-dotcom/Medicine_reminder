package com.medicinereminder.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medicinereminder.dto.MedicineDTO;
import com.medicinereminder.entity.Medicine;
import com.medicinereminder.entity.User;
import com.medicinereminder.repository.MedicineRepository;
import com.medicinereminder.service.MedicineService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class MedicineServiceImpl implements MedicineService {
    
    private final MedicineRepository medicineRepository;
    private final ObjectMapper objectMapper;
    
    public MedicineServiceImpl(MedicineRepository medicineRepository, ObjectMapper objectMapper) {
        this.medicineRepository = medicineRepository;
        this.objectMapper = objectMapper;
    }
    
    @Override
    @Transactional
    public Medicine createMedicine(MedicineDTO dto, User user) {
        String reminderTimesJson = null;
        if (dto.getReminderTimes() != null && !dto.getReminderTimes().isEmpty()) {
            try {
                reminderTimesJson = objectMapper.writeValueAsString(dto.getReminderTimes());
            } catch (JsonProcessingException e) {
                reminderTimesJson = "[]";
            }
        }
        
        Medicine medicine = Medicine.builder()
                .name(dto.getName())
                .dosage(dto.getDosage())
                .frequency(dto.getFrequency())
                .reminderTimes(reminderTimesJson)
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .notes(dto.getNotes())
                .user(user)
                .active(true)
                .build();
        
        return medicineRepository.save(medicine);
    }
    
    @Override
    @Transactional
    public Medicine updateMedicine(Long id, MedicineDTO dto, User user) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found"));
        
        if (!medicine.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        
        String reminderTimesJson = null;
        if (dto.getReminderTimes() != null && !dto.getReminderTimes().isEmpty()) {
            try {
                reminderTimesJson = objectMapper.writeValueAsString(dto.getReminderTimes());
            } catch (JsonProcessingException e) {
                reminderTimesJson = medicine.getReminderTimes();
            }
        } else {
            reminderTimesJson = medicine.getReminderTimes();
        }
        
        medicine.setName(dto.getName());
        medicine.setDosage(dto.getDosage());
        medicine.setFrequency(dto.getFrequency());
        medicine.setReminderTimes(reminderTimesJson);
        medicine.setStartDate(dto.getStartDate());
        medicine.setEndDate(dto.getEndDate());
        medicine.setNotes(dto.getNotes());
        
        return medicineRepository.save(medicine);
    }
    
    @Override
    @Transactional
    public void deleteMedicine(Long id, User user) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found"));
        
        if (!medicine.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        
        medicine.setActive(false);
        medicineRepository.save(medicine);
    }
    
    @Override
    public List<Medicine> findByUser(User user) {
        return medicineRepository.findByUserAndActive(user, true);
    }
    
    @Override
    public List<Medicine> findActiveMedicines(User user) {
        return medicineRepository.findActiveMedicinesForUser(user, LocalDate.now());
    }
    
    @Override
    public List<Medicine> findTodayMedicines(User user) {
        return medicineRepository.findActiveMedicinesForUser(user, LocalDate.now());
    }
    
    @Override
    public Medicine findById(Long id) {
        return medicineRepository.findById(id).orElse(null);
    }
    
    @Override
    public long countActiveByUser(User user) {
        return medicineRepository.countActiveByUser(user);
    }
    
    @Override
    public List<Medicine> findAllActive() {
        return medicineRepository.findAllActive();
    }
}

package com.medicinereminder.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medicinereminder.dto.MedicineDTO;
import com.medicinereminder.entity.Medicine;
import com.medicinereminder.entity.Prescription;
import com.medicinereminder.entity.ReminderStatus;
import com.medicinereminder.entity.Status;
import com.medicinereminder.entity.User;
import com.medicinereminder.service.MedicineService;
import com.medicinereminder.service.PrescriptionService;
import com.medicinereminder.service.ReminderStatusService;
import com.medicinereminder.service.UserService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    
    private final UserService userService;
    private final MedicineService medicineService;
    private final ReminderStatusService reminderStatusService;
    private final PrescriptionService prescriptionService;
    private final ObjectMapper objectMapper;
    
    public UserController(UserService userService, MedicineService medicineService, 
                        ReminderStatusService reminderStatusService,
                        PrescriptionService prescriptionService,
                        ObjectMapper objectMapper) {
        this.userService = userService;
        this.medicineService = medicineService;
        this.reminderStatusService = reminderStatusService;
        this.prescriptionService = prescriptionService;
        this.objectMapper = objectMapper;
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
            
            List<Medicine> todayMedicines = medicineService.findTodayMedicines(user);
            long activeCount = medicineService.countActiveByUser(user);
            
            LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
            LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
            
            long todayTaken = reminderStatusService.countByUserAndStatus(user, Status.TAKEN, startOfDay, endOfDay);
            long todayMissed = reminderStatusService.countByUserAndStatus(user, Status.MISSED, startOfDay, endOfDay);
            
            model.addAttribute("user", user);
            model.addAttribute("todayMedicinesCount", todayMedicines.size());
            model.addAttribute("activeRemindersCount", activeCount);
            model.addAttribute("todayTakenCount", todayTaken);
            model.addAttribute("todayMissedCount", todayMissed);
            model.addAttribute("recentActivity", List.of());
            
            return "dashboard";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error loading dashboard: " + e.getMessage());
            return "error";
        }
    }
    
    @GetMapping("/medicines")
    public String medicines(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        List<Medicine> medicines = medicineService.findByUser(user);
        
        List<MedicineDTO> medicineDTOs = new ArrayList<>();
        for (Medicine medicine : medicines) {
            MedicineDTO dto = new MedicineDTO();
            dto.setId(medicine.getId());
            dto.setName(medicine.getName());
            dto.setDosage(medicine.getDosage());
            dto.setFrequency(medicine.getFrequency());
            dto.setStartDate(medicine.getStartDate());
            dto.setEndDate(medicine.getEndDate());
            dto.setNotes(medicine.getNotes());
            dto.setActive(medicine.getActive());
            
            if (medicine.getReminderTimes() != null) {
                try {
                    List<String> times = objectMapper.readValue(medicine.getReminderTimes(), 
                            new TypeReference<List<String>>() {});
                    dto.setReminderTimes(times);
                } catch (Exception e) {
                    dto.setReminderTimes(new ArrayList<>());
                }
            }
            medicineDTOs.add(dto);
        }
        
        model.addAttribute("medicines", medicineDTOs);
        return "medicines";
    }
    
    @GetMapping("/medicines/new")
    public String newMedicine(Model model) {
        model.addAttribute("medicineDTO", new MedicineDTO());
        model.addAttribute("frequencies", com.medicinereminder.entity.Frequency.values());
        return "medicine-form";
    }
    
    @PostMapping("/medicines")
    public String createMedicine(@Valid @ModelAttribute MedicineDTO medicineDTO,
                                BindingResult result,
                                @AuthenticationPrincipal UserDetails userDetails,
                                Model model) {
        if (result.hasErrors()) {
            model.addAttribute("frequencies", com.medicinereminder.entity.Frequency.values());
            return "medicine-form";
        }
        
        User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        
        if (medicineDTO.getStartDate() == null) {
            medicineDTO.setStartDate(LocalDate.now());
        }
        
        medicineService.createMedicine(medicineDTO, user);
        return "redirect:/medicines?success";
    }
    
    @GetMapping("/medicines/{id}/edit")
    public String editMedicine(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        Medicine medicine = medicineService.findById(id);
        
        if (medicine == null || !medicine.getUser().getId().equals(user.getId())) {
            return "redirect:/medicines?error";
        }
        
        MedicineDTO dto = new MedicineDTO();
        dto.setId(medicine.getId());
        dto.setName(medicine.getName());
        dto.setDosage(medicine.getDosage());
        dto.setFrequency(medicine.getFrequency());
        dto.setStartDate(medicine.getStartDate());
        dto.setEndDate(medicine.getEndDate());
        dto.setNotes(medicine.getNotes());
        
        if (medicine.getReminderTimes() != null) {
            try {
                List<String> times = objectMapper.readValue(medicine.getReminderTimes(), 
                        new TypeReference<List<String>>() {});
                dto.setReminderTimes(times);
            } catch (Exception e) {
                dto.setReminderTimes(new ArrayList<>());
            }
        }
        
        model.addAttribute("medicineDTO", dto);
        model.addAttribute("frequencies", com.medicinereminder.entity.Frequency.values());
        return "medicine-form";
    }
    
    @PostMapping("/medicines/{id}")
    public String updateMedicine(@PathVariable Long id,
                                @Valid @ModelAttribute MedicineDTO medicineDTO,
                                BindingResult result,
                                @AuthenticationPrincipal UserDetails userDetails,
                                Model model) {
        if (result.hasErrors()) {
            model.addAttribute("frequencies", com.medicinereminder.entity.Frequency.values());
            return "medicine-form";
        }
        
        User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        medicineService.updateMedicine(id, medicineDTO, user);
        return "redirect:/medicines?success";
    }
    
    @GetMapping("/medicines/{id}/delete")
    public String deleteMedicine(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        medicineService.deleteMedicine(id, user);
        return "redirect:/medicines?deleted";
    }
    
    @PostMapping("/medicines/{id}/take")
    @ResponseBody
    public String markAsTaken(@PathVariable Long id,
                             @RequestParam String scheduledTime,
                             @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        LocalDateTime scheduled = LocalDateTime.parse(scheduledTime);
        reminderStatusService.markAsTaken(id, scheduled, user);
        return "success";
    }
    
    @PostMapping("/medicines/{id}/miss")
    @ResponseBody
    public String markAsMissed(@PathVariable Long id,
                              @RequestParam String scheduledTime,
                              @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        LocalDateTime scheduled = LocalDateTime.parse(scheduledTime);
        reminderStatusService.markAsMissed(id, scheduled, user);
        return "success";
    }
    
    @GetMapping("/prescriptions")
    public String prescriptions(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        List<Prescription> prescriptions = prescriptionService.findByUser(user);
        model.addAttribute("prescriptions", prescriptions);
        return "prescriptions";
    }
    
    @PostMapping("/prescriptions/upload")
    public String uploadPrescription(@RequestParam MultipartFile file,
                                    @AuthenticationPrincipal UserDetails userDetails,
                                    RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Please select a file to upload");
            return "redirect:/prescriptions";
        }
        
        try {
            User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
            prescriptionService.uploadPrescription(file, user);
            redirectAttributes.addFlashAttribute("message", "Prescription uploaded successfully");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to upload file: " + e.getMessage());
        }
        
        return "redirect:/prescriptions";
    }
    
    @GetMapping("/prescriptions/{id}/download")
    public ResponseEntity<byte[]> downloadPrescription(@PathVariable Long id,
                                                       @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        Prescription prescription = prescriptionService.findById(id);
        
        if (prescription == null || !prescription.getUser().getId().equals(user.getId())) {
            return ResponseEntity.notFound().build();
        }
        
        try {
            byte[] fileData = prescriptionService.getFileData(id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + prescription.getOriginalFileName() + "\"")
                    .contentType(MediaType.parseMediaType(prescription.getFileType()))
                    .body(fileData);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/prescriptions/{id}/delete")
    public String deletePrescription(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        prescriptionService.deletePrescription(id, user);
        return "redirect:/prescriptions?deleted";
    }
}

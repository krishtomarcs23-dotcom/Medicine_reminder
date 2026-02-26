package com.medicinereminder.controller;

import com.medicinereminder.dto.DashboardDTO;
import com.medicinereminder.entity.Medicine;
import com.medicinereminder.entity.Prescription;
import com.medicinereminder.entity.ReminderStatus;
import com.medicinereminder.entity.Status;
import com.medicinereminder.entity.User;
import com.medicinereminder.service.MedicineService;
import com.medicinereminder.service.PrescriptionService;
import com.medicinereminder.service.ReminderStatusService;
import com.medicinereminder.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    private final UserService userService;
    private final MedicineService medicineService;
    private final PrescriptionService prescriptionService;
    private final ReminderStatusService reminderStatusService;
    
    public AdminController(UserService userService, MedicineService medicineService,
                        PrescriptionService prescriptionService,
                        ReminderStatusService reminderStatusService) {
        this.userService = userService;
        this.medicineService = medicineService;
        this.prescriptionService = prescriptionService;
        this.reminderStatusService = reminderStatusService;
    }
    
    @GetMapping
    public String adminDashboard(Model model) {
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalDateTime.now().toLocalTime().plusMinutes(1));
        
        User admin = userService.findAll().stream()
                .filter(u -> "ROLE_ADMIN".equals(u.getRole()))
                .findFirst()
                .orElse(null);
        
        long totalUsers = userService.count();
        long totalMedicines = medicineService.findAllActive().size();
        long todayTaken = 0;
        long todayMissed = 0;
        
        if (admin != null) {
            todayTaken = reminderStatusService.countByUserAndStatus(admin, Status.TAKEN, startOfDay, endOfDay);
            todayMissed = reminderStatusService.countByUserAndStatus(admin, Status.MISSED, startOfDay, endOfDay);
        }
        
        DashboardDTO dashboard = DashboardDTO.builder()
                .totalUsers(totalUsers)
                .activeReminders(totalMedicines)
                .todayTaken(todayTaken)
                .todayMissed(todayMissed)
                .totalMedicines(totalMedicines)
                .totalPrescriptions(prescriptionService.count())
                .build();
        
        model.addAttribute("dashboard", dashboard);
        return "admin-dashboard";
    }
    
    @GetMapping("/users")
    public String users(Model model, @RequestParam(required = false) String search) {
        List<User> users;
        if (search != null && !search.isEmpty()) {
            users = userService.searchUsers(search);
        } else {
            users = userService.findAll();
        }
        
        model.addAttribute("users", users);
        model.addAttribute("search", search);
        return "admin-users";
    }
    
    @PostMapping("/users/{id}/block")
    public String blockUser(@PathVariable Long id) {
        userService.blockUser(id);
        return "redirect:/admin/users?blocked";
    }
    
    @PostMapping("/users/{id}/unblock")
    public String unblockUser(@PathVariable Long id) {
        userService.unblockUser(id);
        return "redirect:/admin/users?unblocked";
    }
    
    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users?deleted";
    }
    
    @GetMapping("/medicines")
    public String allMedicines(Model model) {
        List<Medicine> medicines = medicineService.findAllActive();
        model.addAttribute("medicines", medicines);
        return "admin-medicines";
    }
    
    @GetMapping("/prescriptions")
    public String allPrescriptions(Model model) {
        List<Prescription> prescriptions = prescriptionService.findByUser(
                userService.findAll().stream().findFirst().orElse(null));
        model.addAttribute("prescriptions", prescriptions);
        return "admin-prescriptions";
    }
}

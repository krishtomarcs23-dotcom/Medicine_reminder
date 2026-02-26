package com.medicinereminder.service.impl;

import com.medicinereminder.entity.Medicine;
import com.medicinereminder.entity.ReminderStatus;
import com.medicinereminder.entity.Status;
import com.medicinereminder.entity.User;
import com.medicinereminder.repository.MedicineRepository;
import com.medicinereminder.repository.ReminderStatusRepository;
import com.medicinereminder.service.EmailService;
import com.medicinereminder.service.ReminderStatusService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReminderStatusServiceImpl implements ReminderStatusService {
    
    private final ReminderStatusRepository reminderStatusRepository;
    private final MedicineRepository medicineRepository;
    private final EmailService emailService;
    
    public ReminderStatusServiceImpl(ReminderStatusRepository reminderStatusRepository, 
                                    MedicineRepository medicineRepository,
                                    EmailService emailService) {
        this.reminderStatusRepository = reminderStatusRepository;
        this.medicineRepository = medicineRepository;
        this.emailService = emailService;
    }
    
    @Override
    @Transactional
    public ReminderStatus markAsTaken(Long medicineId, LocalDateTime scheduledTime, User user) {
        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new RuntimeException("Medicine not found"));
        
        ReminderStatus status = reminderStatusRepository
                .findByMedicineIdAndScheduledTimeAndUser(medicineId, scheduledTime, user)
                .orElse(ReminderStatus.builder()
                        .medicine(medicine)
                        .scheduledTime(scheduledTime)
                        .user(user)
                        .status(Status.PENDING)
                        .build());
        
        status.setStatus(Status.TAKEN);
        status.setActionTime(LocalDateTime.now());
        
        return reminderStatusRepository.save(status);
    }
    
    @Override
    @Transactional
    public ReminderStatus markAsMissed(Long medicineId, LocalDateTime scheduledTime, User user) {
        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new RuntimeException("Medicine not found"));
        
        ReminderStatus status = reminderStatusRepository
                .findByMedicineIdAndScheduledTimeAndUser(medicineId, scheduledTime, user)
                .orElse(ReminderStatus.builder()
                        .medicine(medicine)
                        .scheduledTime(scheduledTime)
                        .user(user)
                        .status(Status.PENDING)
                        .build());
        
        status.setStatus(Status.MISSED);
        status.setActionTime(LocalDateTime.now());
        
        ReminderStatus saved = reminderStatusRepository.save(status);
        emailService.sendMissedMedicineEmail(user, medicine);
        
        return saved;
    }
    
    @Override
    public List<ReminderStatus> findByUserAndDateRange(User user, LocalDateTime start, LocalDateTime end) {
        return reminderStatusRepository.findByUserAndDateRange(user, start, end);
    }
    
    @Override
    public List<ReminderStatus> findPendingReminders() {
        return reminderStatusRepository.findPendingReminders(LocalDateTime.now().minusMinutes(30));
    }
    
    @Override
    public long countByUserAndStatus(User user, Status status, LocalDateTime start, LocalDateTime end) {
        return reminderStatusRepository.countByUserAndStatusAndDateRange(user, status, start, end);
    }
    
    @Override
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void processMissedReminders() {
        LocalDateTime now = LocalDateTime.now();
        List<ReminderStatus> pendingReminders = reminderStatusRepository.findPendingReminders(now);
        
        for (ReminderStatus reminder : pendingReminders) {
            if (reminder.getScheduledTime().isBefore(now.minusMinutes(30))) {
                reminder.setStatus(Status.MISSED);
                reminder.setActionTime(now);
                reminderStatusRepository.save(reminder);
                
                emailService.sendMissedMedicineEmail(reminder.getUser(), reminder.getMedicine());
            }
        }
    }
}

package com.medicinereminder.service;

import com.medicinereminder.entity.ReminderStatus;
import com.medicinereminder.entity.Status;
import com.medicinereminder.entity.User;
import java.time.LocalDateTime;
import java.util.List;

public interface ReminderStatusService {
    ReminderStatus markAsTaken(Long medicineId, LocalDateTime scheduledTime, User user);
    ReminderStatus markAsMissed(Long medicineId, LocalDateTime scheduledTime, User user);
    List<ReminderStatus> findByUserAndDateRange(User user, LocalDateTime start, LocalDateTime end);
    List<ReminderStatus> findPendingReminders();
    long countByUserAndStatus(User user, Status status, LocalDateTime start, LocalDateTime end);
    void processMissedReminders();
}

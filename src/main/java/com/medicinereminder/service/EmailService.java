package com.medicinereminder.service;

import com.medicinereminder.entity.Medicine;
import com.medicinereminder.entity.User;

public interface EmailService {
    void sendWelcomeEmail(User user);
    void sendReminderEmail(User user, Medicine medicine, String time);
    void sendMissedMedicineEmail(User user, Medicine medicine);
}

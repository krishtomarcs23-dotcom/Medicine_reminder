package com.medicinereminder.service.impl;

import com.medicinereminder.entity.Medicine;
import com.medicinereminder.entity.User;
import com.medicinereminder.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    
    private final JavaMailSender mailSender;
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);
    
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    @Override
    @Async
    public void sendWelcomeEmail(User user) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(user.getEmail());
            helper.setSubject("Welcome to Medicine Reminder!");
            helper.setText(getWelcomeEmailContent(user), true);
            
            mailSender.send(message);
            log.info("Welcome email sent to: {}", user.getEmail());
        } catch (MessagingException e) {
            log.error("Failed to send welcome email to: {}", user.getEmail(), e);
        }
    }
    
    @Override
    @Async
    public void sendReminderEmail(User user, Medicine medicine, String time) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(user.getEmail());
            helper.setSubject("Medicine Reminder - " + medicine.getName());
            helper.setText(getReminderEmailContent(user, medicine, time), true);
            
            mailSender.send(message);
            log.info("Reminder email sent to: {} for medicine: {}", user.getEmail(), medicine.getName());
        } catch (MessagingException e) {
            log.error("Failed to send reminder email to: {}", user.getEmail(), e);
        }
    }
    
    @Override
    @Async
    public void sendMissedMedicineEmail(User user, Medicine medicine) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(user.getEmail());
            helper.setSubject("Alert: Missed Medicine - " + medicine.getName());
            helper.setText(getMissedMedicineEmailContent(user, medicine), true);
            
            mailSender.send(message);
            log.info("Missed medicine email sent to: {}", user.getEmail());
        } catch (MessagingException e) {
            log.error("Failed to send missed medicine email to: {}", user.getEmail(), e);
        }
    }
    
    private String getWelcomeEmailContent(User user) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f8fafc; padding: 20px; }" +
                ".container { max-width: 600px; margin: 0 auto; background: white; border-radius: 12px; padding: 40px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }" +
                "h1 { color: #0ea5e9; margin-bottom: 20px; }" +
                "p { color: #334155; line-height: 1.6; }" +
                ".button { display: inline-block; background: #0ea5e9; color: white; padding: 12px 24px; border-radius: 8px; text-decoration: none; margin-top: 20px; }" +
                ".footer { margin-top: 30px; padding-top: 20px; border-top: 1px solid #e2e8f0; color: #64748b; font-size: 14px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<h1>Welcome to Medicine Reminder, " + user.getName() + "!</h1>" +
                "<p>Thank you for registering with Medicine Reminder. We're here to help you stay on track with your medication schedule.</p>" +
                "<p>With our application, you can:</p>" +
                "<ul>" +
                "<li>Set up medicine reminders</li>" +
                "<li>Track your daily medications</li>" +
                "<li>Upload and manage prescriptions</li>" +
                "<li>Receive email notifications</li>" +
                "</ul>" +
                "<p>Get started by logging in to your dashboard.</p>" +
                "<div class='footer'>" +
                "<p>Stay healthy!<br>The Medicine Reminder Team</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
    
    private String getReminderEmailContent(User user, Medicine medicine, String time) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f8fafc; padding: 20px; }" +
                ".container { max-width: 600px; margin: 0 auto; background: white; border-radius: 12px; padding: 40px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }" +
                "h1 { color: #0ea5e9; margin-bottom: 20px; }" +
                ".medicine-card { background: #f0f9ff; border-left: 4px solid #0ea5e9; padding: 20px; border-radius: 8px; margin: 20px 0; }" +
                "p { color: #334155; line-height: 1.6; }" +
                ".footer { margin-top: 30px; padding-top: 20px; border-top: 1px solid #e2e8f0; color: #64748b; font-size: 14px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<h1>Medicine Reminder</h1>" +
                "<p>Hello " + user.getName() + ",</p>" +
                "<p>It's time to take your medicine:</p>" +
                "<div class='medicine-card'>" +
                "<h2>" + medicine.getName() + "</h2>" +
                "<p><strong>Dosage:</strong> " + medicine.getDosage() + "</p>" +
                "<p><strong>Time:</strong> " + time + "</p>" +
                "</div>" +
                "<p>Please log in to the application to mark this as taken.</p>" +
                "<div class='footer'>" +
                "<p>This is an automated reminder from Medicine Reminder.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
    
    private String getMissedMedicineEmailContent(User user, Medicine medicine) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f8fafc; padding: 20px; }" +
                ".container { max-width: 600px; margin: 0 auto; background: white; border-radius: 12px; padding: 40px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }" +
                "h1 { color: #ef4444; margin-bottom: 20px; }" +
                ".alert-card { background: #fef2f2; border-left: 4px solid #ef4444; padding: 20px; border-radius: 8px; margin: 20px 0; }" +
                "p { color: #334155; line-height: 1.6; }" +
                ".footer { margin-top: 30px; padding-top: 20px; border-top: 1px solid #e2e8f0; color: #64748b; font-size: 14px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<h1>Alert: Missed Medicine</h1>" +
                "<p>Hello " + user.getName() + ",</p>" +
                "<p>We noticed you missed your medicine:</p>" +
                "<div class='alert-card'>" +
                "<h2>" + medicine.getName() + "</h2>" +
                "<p><strong>Dosage:</strong> " + medicine.getDosage() + "</p>" +
                "</div>" +
                "<p>Please take your medicine as soon as possible and consult your doctor if you have any concerns.</p>" +
                "<div class='footer'>" +
                "<p>This is an important health reminder from Medicine Reminder.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}

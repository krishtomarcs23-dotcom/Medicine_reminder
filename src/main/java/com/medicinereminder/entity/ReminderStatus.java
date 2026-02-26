package com.medicinereminder.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reminder_status")
public class ReminderStatus {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
    
    @Column(name = "scheduled_time", nullable = false)
    private LocalDateTime scheduledTime;
    
    @Column(name = "action_time")
    private LocalDateTime actionTime;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Medicine getMedicine() { return medicine; }
    public void setMedicine(Medicine medicine) { this.medicine = medicine; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public LocalDateTime getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(LocalDateTime scheduledTime) { this.scheduledTime = scheduledTime; }
    public LocalDateTime getActionTime() { return actionTime; }
    public void setActionTime(LocalDateTime actionTime) { this.actionTime = actionTime; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public static ReminderStatusBuilder builder() {
        return new ReminderStatusBuilder();
    }
    
    public static class ReminderStatusBuilder {
        private ReminderStatus reminderStatus = new ReminderStatus();
        public ReminderStatusBuilder id(Long id) { reminderStatus.id = id; return this; }
        public ReminderStatusBuilder medicine(Medicine medicine) { reminderStatus.medicine = medicine; return this; }
        public ReminderStatusBuilder status(Status status) { reminderStatus.status = status; return this; }
        public ReminderStatusBuilder scheduledTime(LocalDateTime scheduledTime) { reminderStatus.scheduledTime = scheduledTime; return this; }
        public ReminderStatusBuilder actionTime(LocalDateTime actionTime) { reminderStatus.actionTime = actionTime; return this; }
        public ReminderStatusBuilder user(User user) { reminderStatus.user = user; return this; }
        public ReminderStatusBuilder createdAt(LocalDateTime createdAt) { reminderStatus.createdAt = createdAt; return this; }
        public ReminderStatus build() { return reminderStatus; }
    }
}

package com.medicinereminder.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "medicines")
public class Medicine {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String name;
    
    @Column(nullable = false, length = 100)
    private String dosage;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Frequency frequency;
    
    @Column(name = "reminder_times", length = 500)
    private String reminderTimes;
    
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Column(length = 1000)
    private String notes;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private Boolean active;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (active == null) {
            active = true;
        }
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }
    public Frequency getFrequency() { return frequency; }
    public void setFrequency(Frequency frequency) { this.frequency = frequency; }
    public String getReminderTimes() { return reminderTimes; }
    public void setReminderTimes(String reminderTimes) { this.reminderTimes = reminderTimes; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    
    public static MedicineBuilder builder() {
        return new MedicineBuilder();
    }
    
    public static class MedicineBuilder {
        private Medicine medicine = new Medicine();
        public MedicineBuilder id(Long id) { medicine.id = id; return this; }
        public MedicineBuilder name(String name) { medicine.name = name; return this; }
        public MedicineBuilder dosage(String dosage) { medicine.dosage = dosage; return this; }
        public MedicineBuilder frequency(Frequency frequency) { medicine.frequency = frequency; return this; }
        public MedicineBuilder reminderTimes(String reminderTimes) { medicine.reminderTimes = reminderTimes; return this; }
        public MedicineBuilder startDate(LocalDate startDate) { medicine.startDate = startDate; return this; }
        public MedicineBuilder endDate(LocalDate endDate) { medicine.endDate = endDate; return this; }
        public MedicineBuilder notes(String notes) { medicine.notes = notes; return this; }
        public MedicineBuilder user(User user) { medicine.user = user; return this; }
        public MedicineBuilder createdAt(LocalDateTime createdAt) { medicine.createdAt = createdAt; return this; }
        public MedicineBuilder active(Boolean active) { medicine.active = active; return this; }
        public Medicine build() { return medicine; }
    }
}

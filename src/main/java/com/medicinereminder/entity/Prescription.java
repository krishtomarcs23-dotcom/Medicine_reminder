package com.medicinereminder.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "prescriptions")
public class Prescription {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String fileName;
    
    @Column(name = "original_file_name", nullable = false)
    private String originalFileName;
    
    @Column(name = "file_type", nullable = false)
    private String fileType;
    
    @Column(name = "file_size", nullable = false)
    private Long fileSize;
    
    @Column(name = "upload_date", nullable = false)
    private LocalDateTime uploadDate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @PrePersist
    protected void onCreate() {
        uploadDate = LocalDateTime.now();
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getOriginalFileName() { return originalFileName; }
    public void setOriginalFileName(String originalFileName) { this.originalFileName = originalFileName; }
    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
    public LocalDateTime getUploadDate() { return uploadDate; }
    public void setUploadDate(LocalDateTime uploadDate) { this.uploadDate = uploadDate; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public static PrescriptionBuilder builder() {
        return new PrescriptionBuilder();
    }
    
    public static class PrescriptionBuilder {
        private Prescription p = new Prescription();
        public PrescriptionBuilder id(Long id) { p.id = id; return this; }
        public PrescriptionBuilder fileName(String fileName) { p.fileName = fileName; return this; }
        public PrescriptionBuilder originalFileName(String originalFileName) { p.originalFileName = originalFileName; return this; }
        public PrescriptionBuilder fileType(String fileType) { p.fileType = fileType; return this; }
        public PrescriptionBuilder fileSize(Long fileSize) { p.fileSize = fileSize; return this; }
        public PrescriptionBuilder uploadDate(LocalDateTime uploadDate) { p.uploadDate = uploadDate; return this; }
        public PrescriptionBuilder user(User user) { p.user = user; return this; }
        public Prescription build() { return p; }
    }
}

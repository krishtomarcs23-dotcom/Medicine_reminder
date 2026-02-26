package com.medicinereminder.repository;

import com.medicinereminder.entity.ReminderStatus;
import com.medicinereminder.entity.Status;
import com.medicinereminder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReminderStatusRepository extends JpaRepository<ReminderStatus, Long> {
    
    List<ReminderStatus> findByUser(User user);
    
    @Query("SELECT r FROM ReminderStatus r JOIN FETCH r.medicine WHERE r.user = :user AND r.scheduledTime >= :start AND r.scheduledTime < :end ORDER BY r.scheduledTime ASC")
    List<ReminderStatus> findByUserAndDateRange(@Param("user") User user, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    @Query("SELECT r FROM ReminderStatus r WHERE r.medicine.id = :medicineId AND r.scheduledTime = :scheduledTime AND r.user = :user")
    Optional<ReminderStatus> findByMedicineIdAndScheduledTimeAndUser(
        @Param("medicineId") Long medicineId, 
        @Param("scheduledTime") LocalDateTime scheduledTime,
        @Param("user") User user);
    
    @Query("SELECT COUNT(r) FROM ReminderStatus r WHERE r.user = :user AND r.status = :status AND r.scheduledTime >= :start AND r.scheduledTime < :end")
    long countByUserAndStatusAndDateRange(
        @Param("user") User user, 
        @Param("status") Status status,
        @Param("start") LocalDateTime start, 
        @Param("end") LocalDateTime end);
    
    @Query("SELECT r FROM ReminderStatus r WHERE r.status = 'PENDING' AND r.scheduledTime < :currentTime")
    List<ReminderStatus> findPendingReminders(@Param("currentTime") LocalDateTime currentTime);
    
    @Query("SELECT COUNT(r) FROM ReminderStatus r WHERE r.status = :status")
    long countByStatus(@Param("status") Status status);
    
    @Query("SELECT r FROM ReminderStatus r WHERE r.scheduledTime >= :start AND r.scheduledTime < :end")
    List<ReminderStatus> findByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}

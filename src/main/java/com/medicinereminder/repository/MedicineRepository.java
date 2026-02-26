package com.medicinereminder.repository;

import com.medicinereminder.entity.Medicine;
import com.medicinereminder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    List<Medicine> findByUser(User user);
    List<Medicine> findByUserAndActive(User user, Boolean active);
    
    @Query("SELECT m FROM Medicine m WHERE m.user = :user AND m.active = true AND m.startDate <= :date AND (m.endDate IS NULL OR m.endDate >= :date)")
    List<Medicine> findActiveMedicinesForUser(@Param("user") User user, @Param("date") LocalDate date);
    
    @Query("SELECT m FROM Medicine m WHERE m.active = true")
    List<Medicine> findAllActive();
    
    @Query("SELECT COUNT(m) FROM Medicine m WHERE m.user = :user AND m.active = true")
    long countActiveByUser(@Param("user") User user);
}

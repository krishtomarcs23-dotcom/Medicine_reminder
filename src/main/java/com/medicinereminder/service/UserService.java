package com.medicinereminder.service;

import com.medicinereminder.dto.RegisterRequest;
import com.medicinereminder.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User register(RegisterRequest request);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    List<User> findAll();
    List<User> searchUsers(String query);
    void blockUser(Long id);
    void unblockUser(Long id);
    void deleteUser(Long id);
    boolean isEmailExists(String email);
    long count();
}

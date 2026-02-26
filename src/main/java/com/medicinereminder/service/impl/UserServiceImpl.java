package com.medicinereminder.service.impl;

import com.medicinereminder.dto.RegisterRequest;
import com.medicinereminder.entity.User;
import com.medicinereminder.repository.UserRepository;
import com.medicinereminder.service.EmailService;
import com.medicinereminder.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }
    
    @Override
    @Transactional
    public User register(RegisterRequest request) {
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("ROLE_USER")
                .enabled(true)
                .blocked(false)
                .build();
        
        User savedUser = userRepository.save(user);
        emailService.sendWelcomeEmail(savedUser);
        return savedUser;
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    @Override
    public List<User> searchUsers(String query) {
        return userRepository.findAll().stream()
                .filter(u -> u.getName().toLowerCase().contains(query.toLowerCase()) ||
                           u.getEmail().toLowerCase().contains(query.toLowerCase()))
                .toList();
    }
    
    @Override
    @Transactional
    public void blockUser(Long id) {
        userRepository.findById(id).ifPresent(user -> {
            user.setBlocked(true);
            userRepository.save(user);
        });
    }
    
    @Override
    @Transactional
    public void unblockUser(Long id) {
        userRepository.findById(id).ifPresent(user -> {
            user.setBlocked(false);
            userRepository.save(user);
        });
    }
    
    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
    @Override
    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }
    
    @Override
    public long count() {
        return userRepository.count();
    }
}

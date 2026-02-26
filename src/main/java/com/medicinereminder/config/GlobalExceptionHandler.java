package com.medicinereminder.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e, Model model) {
        model.addAttribute("error", "File size exceeds maximum limit of 10MB");
        return "error";
    }
    
    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException e, Model model) {
        logger.error("Runtime error on dashboard: ", e);
        model.addAttribute("error", e.getMessage());
        return "error";
    }
    
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        logger.error("Unexpected error: ", e);
        model.addAttribute("error", "An unexpected error occurred: " + e.getMessage());
        return "error";
    }
}

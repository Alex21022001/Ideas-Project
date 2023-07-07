package com.alexsitiy.ideas.project.controller;

import com.alexsitiy.ideas.project.dto.ValidationErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlingControllerAdvice {

    @ExceptionHandler({BindException.class})
    public ResponseEntity<ValidationErrorResponse> handleBindException(BindException bindException) {
        return ResponseEntity.badRequest().body(ValidationErrorResponse.of(bindException.getBindingResult()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorResponse> handleConstraintViolationException(ConstraintViolationException violationException) {
        return ResponseEntity.badRequest().build();
    }
}

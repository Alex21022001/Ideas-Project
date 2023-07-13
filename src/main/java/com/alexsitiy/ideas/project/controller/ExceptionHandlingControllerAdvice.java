package com.alexsitiy.ideas.project.controller;

import com.alexsitiy.ideas.project.dto.ValidationErrorResponse;
import com.alexsitiy.ideas.project.dto.error.FileErrorResponse;
import com.alexsitiy.ideas.project.exception.UploadingFileException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestControllerAdvice
public class ExceptionHandlingControllerAdvice {

    @ExceptionHandler({BindException.class})
    public ResponseEntity<ValidationErrorResponse> handleBindException(BindException bindException) {
        return ResponseEntity.badRequest().body(ValidationErrorResponse.of(bindException.getBindingResult()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException violationException) {

        Set<ConstraintViolation<?>> constraintViolations = violationException.getConstraintViolations();

        // Process the constraint violations and retrieve validation data
        List<String> validationErrors = new ArrayList<>();
        for (ConstraintViolation<?> violation : constraintViolations) {
            String errorMessage = violation.getMessage();
            String propertyPath = violation.getPropertyPath().toString();
            // You can access more details like the invalid value, constraint type, etc.

            // Add the validation error message to the list
            validationErrors.add(propertyPath + ": " + errorMessage);
        }

        return ResponseEntity.badRequest().body(validationErrors);
    }

    @ExceptionHandler({UploadingFileException.class})
    public ResponseEntity<FileErrorResponse> handleUploadingFileException(UploadingFileException fileException) {
        return ResponseEntity.internalServerError().body(FileErrorResponse.of(fileException));
    }
}

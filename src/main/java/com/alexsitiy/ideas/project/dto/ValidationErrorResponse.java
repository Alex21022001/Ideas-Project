package com.alexsitiy.ideas.project.dto;

import lombok.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Value
public class ValidationErrorResponse {

    List<Violation> violations;

    public static ValidationErrorResponse of(BindingResult bindingResult) {
        List<Violation> violations = new ArrayList<>();

        bindingResult.getFieldErrors().forEach(fieldError -> {
            Object rejectedValue;
            if (fieldError.getRejectedValue() instanceof MultipartFile) {
                rejectedValue = ((MultipartFile) fieldError.getRejectedValue()).getOriginalFilename();
            } else
                rejectedValue = fieldError.getRejectedValue();

            violations.add(new Violation(
                    fieldError.getDefaultMessage(),
                    fieldError.getField(),
                    rejectedValue
            ));
        });

        return new ValidationErrorResponse(violations);
    }

    @Value
    static class Violation {
        String message;
        String field;
        Object rejectedValue;
    }
}

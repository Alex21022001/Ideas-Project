package com.alexsitiy.ideas.project.dto;

import lombok.Value;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

@Value
public class ErrorResponse {

    List<Violation> violations;

    public static ErrorResponse of(BindingResult bindingResult){
        List<Violation> violations = new ArrayList<>();

        bindingResult.getFieldErrors().forEach(fieldError -> {
            violations.add(new Violation(
                    fieldError.getDefaultMessage(),
                    fieldError.getField(),
                    fieldError.getRejectedValue()
            ));
        });

        return new ErrorResponse(violations);
    }

    @Value
    static class Violation{
        String message;
        String field;
        Object rejectedValue;
    }
}

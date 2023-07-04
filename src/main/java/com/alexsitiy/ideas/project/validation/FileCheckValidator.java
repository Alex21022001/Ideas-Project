package com.alexsitiy.ideas.project.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

public class FileCheckValidator implements ConstraintValidator<FileCheck, MultipartFile> {

    private boolean nullable;
    private long maxSize;
    private String[] allowContentTypes;

    @Override
    public void initialize(FileCheck constraintAnnotation) {
        nullable = constraintAnnotation.nullable();
        maxSize = constraintAnnotation.maxSize();
        allowContentTypes = constraintAnnotation.contentType();
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (!nullable) {
            return !value.isEmpty() && checkSizeAndType(value);
        } else {
            if (value.isEmpty()) {
                return true;
            } else {
                return checkSizeAndType(value);
            }
        }
    }

    private boolean checkSizeAndType(MultipartFile file) {
        return file.getSize() < maxSize && checkContentType(file.getContentType());
    }

    private boolean checkContentType(String contentType) {
        return Arrays.stream(allowContentTypes).anyMatch(s -> s.equals(contentType));
    }
}


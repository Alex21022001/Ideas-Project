package com.alexsitiy.ideas.project.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = FileCheckValidator.class)
public @interface FileCheck {

    boolean nullable() default true;

    long maxSize() default 500000;

    String[] contentType();

    String message() default "Invalid File";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}

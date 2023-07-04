package com.alexsitiy.ideas.project.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = TitleCheckValidator.class)
public @interface TitleCheck {

    String message() default "Title must be unique";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}

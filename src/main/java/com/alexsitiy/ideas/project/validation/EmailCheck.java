package com.alexsitiy.ideas.project.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = EmailCheckValidator.class)
public @interface EmailCheck {

    String message() default "Such an email is already used";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}

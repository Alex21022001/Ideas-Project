package com.alexsitiy.ideas.project.validation;

import com.alexsitiy.ideas.project.repository.ProjectRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TitleCheckValidator implements ConstraintValidator<TitleCheck,String> {

    private final ProjectRepository projectRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return projectRepository.findByTitle(value)
                .isEmpty();
    }
}

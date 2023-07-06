package com.alexsitiy.ideas.project.dto.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
public class ErrorMessage {
    private String error;
    private ErrorType errorType;
}

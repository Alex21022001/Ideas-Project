package com.alexsitiy.ideas.project.security;

import com.alexsitiy.ideas.project.validation.EmailCheck;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RegisterRequest {
    @NotBlank
    @Size(min = 2)
    private String firstname;

    @NotBlank
    @Size(min = 2)
    private String lastname;

    @Email
    @EmailCheck
    private String username;

    @NotBlank
    @Min(3)
    private String password;
}

package com.alexsitiy.ideas.project.security;

import com.alexsitiy.ideas.project.validation.EmailCheck;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RegisterRequest {
    @NotNull
    @NotBlank
    @Size(min = 2)
    private String firstname;

    @NotNull
    @NotBlank
    @Size(min = 2)
    private String lastname;

    @Email
    @EmailCheck
    private String username;

    @NotNull
    @NotBlank
    @Min(3)
    private String password;
}

package com.example.crypto_tracker.dto.user;

import com.example.crypto_tracker.enums.UserRole;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateUserDTO {
    @NotNull
    @Email(message = "Invalid email format")
    private String email;
    @NotNull
    //TODO: Add password validator
    private String password;
    @Nullable
    private String firstName;
    @Nullable
    private String lastName;
    @NotNull
    private UserRole role;
}

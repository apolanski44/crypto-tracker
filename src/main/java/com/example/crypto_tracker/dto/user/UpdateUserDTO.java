package com.example.crypto_tracker.dto.user;

import com.example.crypto_tracker.enums.UserRole;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UpdateUserDTO {
    @Nullable
    @Email(message = "Invalid email format")
    private String email;
    @Nullable
    //TODO: Add password validator
    private String password;
    @Nullable
    private String firstName;
    @Nullable
    private String lastName;
    @Nullable
    private UserRole role;
}

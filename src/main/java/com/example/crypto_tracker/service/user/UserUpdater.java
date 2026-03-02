package com.example.crypto_tracker.service.user;

import com.example.crypto_tracker.dto.user.UpdateUserDTO;
import com.example.crypto_tracker.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//We can use MapStruct, but for testing purposes I've created helper service
@Component
@RequiredArgsConstructor public class UserUpdater {
    private final PasswordEncoder passwordEncoder;

    public void updateEntityFromDto(UpdateUserDTO dto, User user) {
        if (dto == null || user == null)
        {
            return;
        }

        if (dto.getEmail() != null)
        {
            user.setEmail(dto.getEmail());
        }

        if (dto.getPassword() != null && !dto.getPassword().isBlank())
        {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if (dto.getFirstName() != null)
        {
            user.setFirstName(dto.getFirstName());
        }

        if (dto.getLastName() != null)
        {
            user.setLastName(dto.getLastName());
        }

        if (dto.getRole() != null)
        {
            user.setRole(dto.getRole());
        }
    }
}

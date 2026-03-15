package com.example.crypto_tracker.service.user;

import com.example.crypto_tracker.dto.user.CreateUserDTO;
import com.example.crypto_tracker.dto.auth.AuthRequest;
import com.example.crypto_tracker.enums.UserRole;
import com.example.crypto_tracker.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toEntity(CreateUserDTO dto) {
       if (dto == null) {
           return null;
       }

       return User.builder()
               .email(dto.getEmail())
               .firstName(dto.getFirstName())
               .lastName(dto.getLastName())
               .role(dto.getRole())
               .accountVerified(true)
               .build();
    }

    public User toEntity(AuthRequest dto) {
        if (dto == null) {
            return null;
        }

        return User.builder()
                .email(dto.getEmail())
                .role(UserRole.ROLE_USER)
                .build();
    }
}

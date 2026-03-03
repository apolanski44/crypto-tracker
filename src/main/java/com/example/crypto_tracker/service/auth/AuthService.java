package com.example.crypto_tracker.service.auth;

import com.example.crypto_tracker.dto.user.CreateUserDTO;
import com.example.crypto_tracker.enums.UserRole;
import com.example.crypto_tracker.model.User;
import com.example.crypto_tracker.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;

    public User register(CreateUserDTO dto) {
        dto.setRole(UserRole.ROLE_USER);

        return userService.createUser(dto);
    }
}

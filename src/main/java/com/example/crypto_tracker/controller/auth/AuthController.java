package com.example.crypto_tracker.controller.auth;

import com.example.crypto_tracker.dto.user.CreateUserDTO;
import com.example.crypto_tracker.model.User;
import com.example.crypto_tracker.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestBody @Valid CreateUserDTO dto) {
        return authService.register(dto);
    }
}

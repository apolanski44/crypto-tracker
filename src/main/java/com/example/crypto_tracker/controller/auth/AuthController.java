package com.example.crypto_tracker.controller.auth;

import com.example.crypto_tracker.dto.auth.AuthRequest;
import com.example.crypto_tracker.dto.auth.AuthResponse;
import com.example.crypto_tracker.dto.auth.VerificationRequest;
import com.example.crypto_tracker.service.auth.LoginService;
import com.example.crypto_tracker.service.auth.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final RegistrationService authService;
    private final LoginService loginService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody @Valid AuthRequest dto) {
        authService.register(dto);
    }

    @PostMapping("/verify")
    public void verify(@RequestBody @Valid VerificationRequest request) {
        authService.confirmEmail(request.getCode());
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest dto) {
        String token = loginService.login(dto);

        return ResponseEntity.ok(new AuthResponse(token));
    }
}

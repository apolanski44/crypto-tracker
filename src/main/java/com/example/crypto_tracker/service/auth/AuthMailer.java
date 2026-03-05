package com.example.crypto_tracker.service.auth;

import com.example.crypto_tracker.service.email.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthMailer {
    private final EmailSenderService emailSenderService;

    @Transactional
    public void sendActivationEmail(String email, String token) {
        String message = "Your activation code: " + token;

        emailSenderService.sendEmail(email, "Account activation", message);
    }
}

package com.example.crypto_tracker.service.auth;

import com.example.crypto_tracker.service.email.EmailSenderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static reactor.core.publisher.Mono.when;

@ExtendWith(MockitoExtension.class)
public class AuthMailerTest {
    @Mock
    private EmailSenderService emailSenderService;

    @InjectMocks
    private AuthMailer authMailer;

    @Test
    void sendActivationEmailPasses() {
        String email = "valid@valid.com";
        String token = "123456";

        authMailer.sendActivationEmail(email, token);

        verify(emailSenderService).sendEmail(
                email,
                "Account activation",
                "Your activation code: 123456"
        );
    }
}

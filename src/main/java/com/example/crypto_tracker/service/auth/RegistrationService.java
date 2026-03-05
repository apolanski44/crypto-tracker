package com.example.crypto_tracker.service.auth;

import com.example.crypto_tracker.dto.auth.AuthRequest;
import com.example.crypto_tracker.exception.UserAlreadyVerifiedException;
import com.example.crypto_tracker.model.User;
import com.example.crypto_tracker.model.VerificationToken;
import com.example.crypto_tracker.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final UserService userService;
    private final VerificationTokenService tokenService;
    private final AuthMailer authMailer;

    @Transactional
    public void register(AuthRequest dto) {
        userService.registerUser(dto)
                .ifPresent(user -> {
                    String token = tokenService.createToken(user);
                    authMailer.sendActivationEmail(user.getEmail(), token);
                });
    }

    @Transactional
    public void confirmEmail(String code) {
        VerificationToken token = tokenService.getValidToken(code);

        User user = token.getUser();

        if (user.isAccountVerified()) {
            throw new UserAlreadyVerifiedException("User has been already verified.");
        }

        user.setAccountVerified(true);

        tokenService.deleteToken(token);
    }
}

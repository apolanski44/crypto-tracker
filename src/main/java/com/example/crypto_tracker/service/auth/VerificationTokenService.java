package com.example.crypto_tracker.service.auth;

import com.example.crypto_tracker.exception.TokenExpiredException;
import com.example.crypto_tracker.exception.TokenNotFoundException;
import com.example.crypto_tracker.model.User;
import com.example.crypto_tracker.model.VerificationToken;
import com.example.crypto_tracker.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VerificationTokenService {
    private final VerificationTokenRepository tokenRepository;

    @Transactional
    public String createToken(User user) {
        String token = UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        VerificationToken verificationToken = VerificationToken.builder()
                .user(user)
                .token(token)
                .build();

        tokenRepository.save(verificationToken);

        return token;
    }

    public VerificationToken getValidToken(String token) {
        VerificationToken vt = tokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenNotFoundException("Token not found."));

        if (vt.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(vt);

            throw new TokenExpiredException("Token has expired.");
        }

        return vt;
    }

    @Transactional
    public void deleteToken(VerificationToken token) {
        if(!tokenRepository.existsByToken(token.getToken())) {
            throw new TokenNotFoundException("Token not found.");
        }

        tokenRepository.delete(token);
    }
}

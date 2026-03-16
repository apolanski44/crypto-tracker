package com.example.crypto_tracker.service.auth;

import com.example.crypto_tracker.exception.TokenExpiredException;
import com.example.crypto_tracker.exception.TokenNotFoundException;
import com.example.crypto_tracker.model.User;
import com.example.crypto_tracker.model.VerificationToken;
import com.example.crypto_tracker.repository.VerificationTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class VerificationTokenServiceTest {
    @Mock
    private VerificationTokenRepository tokenRepository;

    @InjectMocks
    private VerificationTokenService verificationTokenService;

    @Test
    void createTokenReturnsToken() {
        User user = User.builder()
                .email("email@test.com")
                .password("password44")
                .build();

        String token = verificationTokenService.createToken(user);

        ArgumentCaptor<VerificationToken> captor =
                ArgumentCaptor.forClass(VerificationToken.class);

        verify(tokenRepository, times(1)).save(captor.capture());

        VerificationToken savedToken = captor.getValue();

        assertThat(token).hasSize(6);
        assertThat(savedToken.getUser()).isEqualTo(user);
        assertThat(savedToken.getToken()).isEqualTo(token);
    }

    @Test
    void getValidTokenReturnsToken() {
        String token = "123456";
        VerificationToken vt = new VerificationToken();

        vt.setToken(token);
        vt.setExpiryDate(LocalDateTime.now().plusHours(5));

        when(tokenRepository.findByToken(token))
                .thenReturn(Optional.of(vt));

        VerificationToken result = verificationTokenService.getValidToken(token);

        verify(tokenRepository, times(1)).findByToken(token);
        verify(tokenRepository, never()).delete(vt);
        assertThat(result).isEqualTo(vt);
    }

    @Test
    void getValidTokenThrowsTokenNotFoundException() {
        String token = "INVALID TOKEN";

        assertThatExceptionOfType(TokenNotFoundException.class)
                .isThrownBy(() -> verificationTokenService.getValidToken(token))
                .withMessage("Token not found.");


        verify(tokenRepository, times(1)).findByToken(token);
        verify(tokenRepository, never()).delete(any());
    }

    @Test
    void getValidTokenThrowsTokenExpiredException() {
        String token = "123456";
        VerificationToken vt = new VerificationToken();

        vt.setToken(token);
        vt.setExpiryDate(LocalDateTime.now().minusHours(5));

        when(tokenRepository.findByToken(token))
                .thenReturn(Optional.of(vt));

        assertThatExceptionOfType(TokenExpiredException.class)
                .isThrownBy(() -> verificationTokenService.getValidToken(token))
                .withMessage("Token has expired.");

        verify(tokenRepository, times(1)).findByToken(token);
        verify(tokenRepository, times(1)).delete(vt);
    }

    @Test
    void deleteTokenPasses() {
        VerificationToken vt = new VerificationToken();

        vt.setToken("123456");

        when(tokenRepository.existsByToken(vt.getToken()))
                .thenReturn(true);

        verificationTokenService.deleteToken(vt);

        verify(tokenRepository, times(1)).delete(vt);
    }

    @Test
    void deleteTokenThrowsTokenNotFoundException() {
        VerificationToken vt = new VerificationToken();

        vt.setToken("123456");

        when(tokenRepository.existsByToken(vt.getToken()))
                .thenReturn(false);

        assertThatExceptionOfType(TokenNotFoundException.class)
                .isThrownBy(() -> verificationTokenService.deleteToken(vt))
                .withMessage("Token not found.");

        verify(tokenRepository, never()).delete(vt);
    }
}

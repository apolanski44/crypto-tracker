package com.example.crypto_tracker.service.auth;

import com.example.crypto_tracker.dto.auth.AuthRequest;
import com.example.crypto_tracker.exception.UserAlreadyVerifiedException;
import com.example.crypto_tracker.model.User;
import com.example.crypto_tracker.model.VerificationToken;
import com.example.crypto_tracker.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private VerificationTokenService verificationTokenService;

    @Mock
    private AuthMailer authMailer;

    @InjectMocks
    private RegistrationService registrationService;

    @Test
    void shouldTriggerRegistrationMethods() {
        String email = "valid@email.com";
        String password = "Valid1234";
        String token = "TOKEN";

        AuthRequest authRequest = new AuthRequest();

        authRequest.setPassword(password);
        authRequest.setEmail(email);

        User user = User.builder()
                .email(email)
                .password(password)
                .build();

        when(userService.registerUser(authRequest))
                .thenReturn(Optional.of(user));

        when(verificationTokenService.createToken(user))
                .thenReturn(token);

        registrationService.register(authRequest);

        verify(userService, times(1)).registerUser(authRequest);
        verify(verificationTokenService, times(1)).createToken(user);
        verify(authMailer, times(1)).sendActivationEmail(email, token);
    }

    @Test
    void registerShouldReturnEmptyValue() {
        String email = "valid@email.com";
        String password = "Valid1234";

        AuthRequest authRequest = new AuthRequest();

        authRequest.setPassword(password);
        authRequest.setEmail(email);

        when(userService.registerUser(authRequest))
                .thenReturn(Optional.empty());

        registrationService.register(authRequest);

        verify(userService, times(1)).registerUser(authRequest);
        verifyNoInteractions(verificationTokenService);
        verifyNoInteractions(authMailer);
    }

    @Test
    void confirmEmailSuccessfullyActivatesEmail() {
        String code = "123456";
        User user = new User();
        VerificationToken token = new VerificationToken();

        user.setAccountVerified(false);
        token.setToken(code);
        token.setUser(user);


        when(verificationTokenService.getValidToken(code))
                .thenReturn(token);

        registrationService.confirmEmail(code);

        verify(verificationTokenService, times(1)).getValidToken(code);
        assertThat(user.isAccountVerified()).isTrue();
        verify(verificationTokenService, times(1)).deleteToken(token);
    }

    @Test
    void confirmEmailThrowsUserAlreadyVerifiedException() {
        String code = "123456";
        User user = new User();
        VerificationToken token = new VerificationToken();

        user.setAccountVerified(true);
        token.setToken(code);
        token.setUser(user);

        when(verificationTokenService.getValidToken(code))
                .thenReturn(token);

        assertThatExceptionOfType(UserAlreadyVerifiedException.class)
                .isThrownBy(() -> registrationService.confirmEmail(code))
                .withMessage("User has been already verified.");

        verify(verificationTokenService, times(1)).getValidToken(code);
        verify(verificationTokenService, never()).deleteToken(token);
    }
}

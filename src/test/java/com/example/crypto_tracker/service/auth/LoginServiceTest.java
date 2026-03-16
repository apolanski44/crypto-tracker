package com.example.crypto_tracker.service.auth;

import com.example.crypto_tracker.dto.auth.AuthRequest;
import com.example.crypto_tracker.model.User;
import com.example.crypto_tracker.repository.UserRepository;
import com.example.crypto_tracker.service.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoginService loginService;

   @Test
    void shouldReturnTokenWhenCredentialsAreValid() {
       String email = "valid@email.com";
       String password = "Valid1234";
       String token = "TOKEN";

       AuthRequest authRequest = new AuthRequest();
       authRequest.setEmail(email);
       authRequest.setPassword(password);

       User user = User.builder()
               .email(email)
               .password(password)
               .build();

       when(userRepository.findByEmail(email))
               .thenReturn(Optional.of(user));

       when(jwtService.generateToken(user))
               .thenReturn(token);

       String result = loginService.login(authRequest);

       assertThat(result).isEqualTo(token);

       verify(authenticationManager).authenticate(
               argThat(auth ->
                       auth instanceof UsernamePasswordAuthenticationToken authToken
                       && authToken.getName().equals(email)
               )
       );
       verify(userRepository).findByEmail(email);
       verify(jwtService).generateToken(user);
   }
}

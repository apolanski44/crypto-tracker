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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.naming.AuthenticationException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


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

       verify(authenticationManager, times(1)).authenticate(
               argThat(auth ->
                       auth instanceof UsernamePasswordAuthenticationToken authToken
                       && authToken.getName().equals(email)
               )
       );
       verify(userRepository, times(1)).findByEmail(email);
       verify(jwtService, times(1)).generateToken(user);
   }

   @Test
    void shouldThrowAuthenticationExceptionWhenAuthenticationFails() {
       String email = "invalid@email.com";
       String password = "inValid1234";

       AuthRequest authRequest = new AuthRequest();
       authRequest.setEmail(email);
       authRequest.setPassword(password);

       doThrow(new BadCredentialsException("Bad credentials"))
               .when(authenticationManager)
               .authenticate(any());

       assertThatThrownBy(() -> loginService.login(authRequest))
               .isInstanceOf(BadCredentialsException.class)
               .hasMessage("Bad credentials");

       verify(authenticationManager, times(1)).authenticate(any());
       verifyNoInteractions(userRepository);
       verifyNoInteractions(jwtService);
   }

   @Test
    void shouldThrowExceptionWhenUserNotFound() {
       String email = "invalid@email.com";
       String password = "inValid1234";

       AuthRequest authRequest = new AuthRequest();
       authRequest.setEmail(email);
       authRequest.setPassword(password);

       doThrow(new BadCredentialsException("Invalid credentials"))
               .when(userRepository)
               .findByEmail(email);

       assertThatThrownBy(() -> userRepository.findByEmail(email))
               .isInstanceOf(BadCredentialsException.class)
               .hasMessage("Invalid credentials");

       verify(userRepository, times(1)).findByEmail(email);
       verifyNoInteractions(jwtService);
   }
}

package ru.programmator.authservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import ru.programmator.authservice.dto.auth.AuthenticationResponse;
import ru.programmator.authservice.dto.auth.LoginRequest;
import ru.programmator.authservice.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class AuthServiceTest {

    private AuthenticationManager authenticationManager;
    private TokenService tokenService;
    private UserService userService;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        authenticationManager = Mockito.mock(AuthenticationManager.class);
        tokenService = Mockito.mock(TokenService.class);
        userService = Mockito.mock(UserService.class);
        authService = new AuthService(userService, tokenService, authenticationManager);
    }

    @Test
    void authenticateUser_shouldReturnAuthenticationResponse() {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("test@example.com")
                .password("password")
                .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken("test@example.com", "password");

        User user = new User();
        user.setEmail("test@example.com");
        user.setFirstName("Test");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(tokenService.generateToken(authentication)).thenReturn("test-token");
        when(userService.findByEmail("test@example.com")).thenReturn(user);

        AuthenticationResponse response = authService.authenticateUser(loginRequest);

        assertEquals("test-token", response.getToken());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("Test", response.getFirstName());
    }
}
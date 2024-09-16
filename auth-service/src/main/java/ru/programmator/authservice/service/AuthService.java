package ru.programmator.authservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.programmator.authservice.dto.auth.AuthenticationResponse;
import ru.programmator.authservice.dto.auth.LoginRequest;
import ru.programmator.authservice.model.User;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticate(loginRequest);
        String token = tokenService.generateToken(authentication);
        User user = userService.findByEmail(loginRequest.getEmail());
        return buildAuthenticationResponse(user, token);
    }

    private Authentication authenticate(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    private AuthenticationResponse buildAuthenticationResponse(User user, String token) {
        return AuthenticationResponse.builder()
                .token(token)
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .build();
    }
}

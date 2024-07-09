package ru.programmator.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import ru.programmator.security.JwtTokenProvider;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;

    public String generateToken(Authentication authentication) {
        return jwtTokenProvider.createToken(
                authentication.getName(),
                getAuthorities(authentication)
        );
    }

    private List<String> getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }
}

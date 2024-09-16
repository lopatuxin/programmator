package ru.programmator.authservice.dto.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponse {

    private String token;
    private String email;
    private String firstName;
}

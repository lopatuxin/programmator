package ru.programmator.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.programmator.dto.BaseResponse;
import ru.programmator.dto.auth.AuthenticationResponse;
import ru.programmator.dto.auth.LoginRequest;
import ru.programmator.dto.auth.UserDTO;
import ru.programmator.model.User;
import ru.programmator.security.JwtTokenProvider;
import ru.programmator.service.UserService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "Register a new user", description = "Регистрация нового пользователя с предоставлением всех необходимых данных.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping("/register")
    public BaseResponse<Object> register(@RequestBody UserDTO userDTO) {
        User user = User.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .password(userDTO.getPassword())
                .email(userDTO.getEmail())
                .phone(userDTO.getPhone())
                .build();

        userService.register(user);

        return BaseResponse.builder()
                .status(HttpStatus.OK)
                .message("Пользователь успешно зарегистрирован")
                .build();
    }

    @Operation(summary = "User login", description = "Вход пользователя в систему с предоставлением email и пароля.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно авторизован",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "401", description = "Неавторизован",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping("/login")
    public BaseResponse<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.createToken(authentication.getName(),
                authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList());

        User user = userService.findByEmail(loginRequest.getEmail());

        return BaseResponse.<AuthenticationResponse>builder()
                .status(HttpStatus.OK)
                .message("Пользователь успешно авторизован")
                .data(AuthenticationResponse.builder()
                        .token(token)
                        .email(user.getEmail())
                        .firstName(user.getFirstName())
                        .build())
                .build();
    }
}

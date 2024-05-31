package ru.programmator.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.programmator.model.User;
import ru.programmator.service.UserService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public void register(@RequestBody User user) {
        userService.register(user);
    }

    @PostMapping("/login")
    public void login() {
        // Spring Security handles login
    }
}

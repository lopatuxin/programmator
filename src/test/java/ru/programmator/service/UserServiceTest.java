package ru.programmator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.programmator.model.User;
import ru.programmator.repository.UserRepository;

import static org.mockito.Mockito.verify;

class UserServiceTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserValidator userValidator;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userValidator = Mockito.mock(UserValidator.class);
        userService = new UserService(userRepository, passwordEncoder, userValidator);
    }

    @Test
    void register_shouldEncodePasswordAndSaveUser() {
        User user = new User();
        user.setPassword("password");

        userService.register(user);

        verify(userValidator).validate(user);
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(user);
    }
}
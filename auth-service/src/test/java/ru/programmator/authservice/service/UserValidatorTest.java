package ru.programmator.authservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.programmator.authservice.exception.UserRegistrationException;
import ru.programmator.authservice.model.User;
import ru.programmator.authservice.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class UserValidatorTest {

    private UserRepository userRepository;
    private UserValidator userValidator;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userValidator = new UserValidator(userRepository);
    }

    @Test
    void validate_shouldThrowException_whenEmailExists() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertThrows(UserRegistrationException.class, () -> userValidator.validate(user));
    }

    @Test
    void validate_shouldThrowException_whenPhoneExists() {
        User user = new User();
        user.setPhone("1234567890");

        when(userRepository.existsByPhone("1234567890")).thenReturn(true);

        assertThrows(UserRegistrationException.class, () -> userValidator.validate(user));
    }
}
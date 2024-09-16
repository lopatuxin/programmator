package ru.programmator.authservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.programmator.authservice.exception.UserRegistrationException;
import ru.programmator.authservice.model.User;
import ru.programmator.authservice.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    public void validate(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserRegistrationException("Email уже зарегистрирован");
        }

        if (userRepository.existsByPhone(user.getPhone())) {
            throw new UserRegistrationException("Телефон уже зарегистрирован");
        }
    }
}

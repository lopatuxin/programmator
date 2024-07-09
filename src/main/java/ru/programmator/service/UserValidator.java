package ru.programmator.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.programmator.exception.UserRegistrationException;
import ru.programmator.model.User;
import ru.programmator.repository.UserRepository;

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

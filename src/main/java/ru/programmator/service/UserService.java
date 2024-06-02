package ru.programmator.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.programmator.exception.UserRegistrationException;
import ru.programmator.model.User;
import ru.programmator.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserRegistrationException("Email уже зарегистрирован");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}

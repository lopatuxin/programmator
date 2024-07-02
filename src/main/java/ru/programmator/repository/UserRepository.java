package ru.programmator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.programmator.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}

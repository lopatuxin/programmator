package ru.programmator.authservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.programmator.authservice.model.Role;
import ru.programmator.authservice.model.User;
import ru.programmator.authservice.repository.UserRepository;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class CustomUserDetailsServiceTest {

    private UserRepository userRepository;
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        RoleConverter roleConverter = new RoleConverter();
        customUserDetailsService = new CustomUserDetailsService(userRepository, roleConverter);
    }

    @Test
    void loadUserByUsername_shouldThrowException_whenUserNotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername("test@example.com"));
    }

    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenUserFound() {
        Role userRole = new Role();
        userRole.setName("ROLE_USER");

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRoles(Set.of(userRole));

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        var userDetails = customUserDetailsService.loadUserByUsername("test@example.com");

        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
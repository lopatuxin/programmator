package ru.programmator.authservice.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import ru.programmator.authservice.model.Role;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RoleConverter {

    public Set<SimpleGrantedAuthority> convertRolesToAuthorities(Set<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
    }
}

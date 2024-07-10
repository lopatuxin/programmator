package ru.programmator.service;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.programmator.model.Role;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoleConverterTest {

    @Test
    void convertRolesToAuthorities_shouldConvertRolesCorrectly() {
        Role userRole = new Role();
        userRole.setName("ROLE_USER");

        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");

        RoleConverter roleConverter = new RoleConverter();
        Set<SimpleGrantedAuthority> authorities = roleConverter.convertRolesToAuthorities(Set.of(userRole, adminRole));

        assertEquals(2, authorities.size());
        assertEquals(Set.of(new SimpleGrantedAuthority("ROLE_USER"), new SimpleGrantedAuthority("ROLE_ADMIN")), authorities);
    }
}
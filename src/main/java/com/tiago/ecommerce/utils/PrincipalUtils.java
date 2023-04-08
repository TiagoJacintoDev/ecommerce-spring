package com.tiago.ecommerce.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class PrincipalUtils {
    private final Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    public boolean isAdmin() {
        return auth
                .getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

    public boolean isUser() {
        return auth
                .getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"));
    }

    public String getName() {
        return auth.getName();
    }
}

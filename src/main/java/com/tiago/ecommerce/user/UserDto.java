package com.tiago.ecommerce.user;

import jakarta.validation.constraints.NotBlank;

public record UserDto(
        @NotBlank String username,
        @NotBlank String password
){}
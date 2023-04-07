package com.tiago.ecommerce.role;

import jakarta.validation.constraints.NotBlank;

public record RoleDto(
        @NotBlank String name
){}

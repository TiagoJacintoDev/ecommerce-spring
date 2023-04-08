package com.tiago.ecommerce.category;

import jakarta.validation.constraints.NotBlank;

public record CategoryDto(
        @NotBlank String name
){}
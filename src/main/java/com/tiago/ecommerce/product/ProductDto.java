package com.tiago.ecommerce.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductDto(
        @NotBlank String name,
        @NotNull Float price,
        String description,
        String imageUrl
){}
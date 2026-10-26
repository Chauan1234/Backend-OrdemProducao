package com.satc.edu.ordem_producao.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProductRequestDTO(
        @NotBlank
        @Size(max = 150)
        String name,

        String description,

        Boolean active
) {
}

package com.satc.edu.ordem_producao.product.dto;

import java.util.UUID;

public record ProductResponseDTO(
        UUID id,
        Integer code,
        String name,
        String description,
        Boolean active
) {
}

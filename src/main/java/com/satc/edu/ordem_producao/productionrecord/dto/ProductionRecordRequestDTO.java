package com.satc.edu.ordem_producao.productionrecord.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotNull;

public record ProductionRecordRequestDTO(
        @NotNull
        @Positive
        Integer quantity,
        String notes
) {
}

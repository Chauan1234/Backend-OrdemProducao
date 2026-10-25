package com.satc.edu.ordem_producao.productionrecord.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductionRecordRequestDTO(
        @NotNull
        @Positive
        Integer quantity,

        String notes
) {
}

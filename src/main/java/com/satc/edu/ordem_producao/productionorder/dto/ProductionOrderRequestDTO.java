package com.satc.edu.ordem_producao.productionorder.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.UUID;

public record ProductionOrderRequestDTO(
        @NotNull
        UUID productId,

        @NotNull
        UUID machineId,

        @NotNull
        @Positive
        Integer targetQuantity,

        @FutureOrPresent
        LocalDate deadline
) {
}

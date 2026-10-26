package api.dto.productionorder;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Representa os dados de entrada para criar uma ordem de produção.
 */

@Schema(name = "ProductionOrderRequest", description = "Dados para criação de uma ordem de produção.")
public record ProductionOrderRequestDTO(
        @NotNull
        @Schema(description = "UUID de um produto existente e ativo.", example = "c37c6e28-9ab2-4b48-9d7d-59bb5152a013")
        UUID productId,

        @NotNull
        @Schema(description = "UUID de uma máquina existente e disponível.", example = "6b9d53f4-c6a8-4d05-8ef5-cf91a1ff323c")
        UUID machineId,

        @NotNull
        @Positive
        @Schema(description = "Quantidade planejada, maior que zero.", example = "500", minimum = "1")
        Integer targetQuantity,

        @FutureOrPresent
        @Schema(description = "Prazo opcional da ordem; deve ser hoje ou uma data futura.", example = "2026-11-15", nullable = true)
        LocalDate deadline
) {
}

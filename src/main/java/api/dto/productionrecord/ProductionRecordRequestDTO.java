package api.dto.productionrecord;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Representa os dados de entrada para registrar produção.
 */

@Schema(name = "ProductionRecordRequest", description = "Dados para registrar produção em uma ordem em andamento.")
public record ProductionRecordRequestDTO(
        @NotNull
        @Positive
        @Schema(description = "Quantidade produzida neste registro, maior que zero.", example = "150", minimum = "1")
        Integer quantity,

        @Schema(description = "Observações opcionais sobre o registro.", example = "Lote produzido sem ocorrências.", nullable = true)
        String notes
) {
}

// CAMADA: DTO
// RESPONSABILIDADE: transportar dados de entrada ou saída da API.
// REGRAS: define o contrato e as validações do payload.
// ROTAS ATENDIDAS: não se aplica.
// SITUAÇÃO: implementada.
package api.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ProductionRecordResponse", description = "Registro de produção criado para uma ordem.")
public record ProductionRecordResponseDTO(
        @Schema(description = "Identificador UUID do registro.", example = "fdb591e9-3038-4ed9-9a13-f509e5225f4f")
        UUID id,
        @Schema(description = "Identificador UUID da ordem associada.", example = "dc4b6f33-cb55-4ba4-8d9e-e2880c7b42f3")
        UUID productionOrderId,

        @Schema(description = "Quantidade registrada.", example = "150")
        Integer quantity,
        @Schema(description = "Data e hora em que o registro foi criado.", example = "2026-10-25T12:00:00")
        LocalDateTime registeredAt,

        @Schema(description = "Observações do registro.", example = "Lote produzido sem ocorrências.", nullable = true)
        String notes
) {
}

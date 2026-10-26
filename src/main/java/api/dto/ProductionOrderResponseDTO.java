// CAMADA: DTO
// RESPONSABILIDADE: transportar dados de entrada ou saída da API.
// REGRAS: define o contrato e as validações do payload.
// ROTAS ATENDIDAS: não se aplica.
// SITUAÇÃO: implementada.
package api.dto;

import api.dto.MachineResponseDTO;
import api.dto.ProductResponseDTO;
import api.model.ProductionOrderStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ProductionOrderResponse", description = "Detalhes completos de uma ordem de produção.")
public record ProductionOrderResponseDTO(
        @Schema(description = "Identificador UUID da ordem.", example = "dc4b6f33-cb55-4ba4-8d9e-e2880c7b42f3")
        UUID id,
        @Schema(description = "Produto vinculado à ordem.")
        ProductResponseDTO product,
        @Schema(description = "Máquina vinculada à ordem.")
        MachineResponseDTO machine,
        @Schema(description = "Quantidade planejada.", example = "500")
        Integer targetQuantity,
        @Schema(description = "Quantidade produzida até o momento.", example = "200")
        Integer producedQuantity,
        @Schema(description = "Estado atual da ordem.", example = "IN_PROGRESS")
        ProductionOrderStatus status,
        @Schema(description = "Data e hora de criação.", example = "2026-10-25T10:00:00")
        LocalDateTime createdAt,
        @Schema(description = "Data e hora de início da produção.", example = "2026-10-25T11:00:00", nullable = true)
        LocalDateTime startedAt,
        @Schema(description = "Data e hora da conclusão.", example = "2026-10-25T12:30:00", nullable = true)
        LocalDateTime completedAt,
        @Schema(description = "Prazo da ordem.", example = "2026-11-15", nullable = true)
        LocalDate deadline
) {
}

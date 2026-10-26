package api.dto.productionorder;

import api.model.ProductionOrderStatus;
import java.time.LocalDate;
import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Representa os dados resumidos de uma ordem usados nas listagens.
 */

@Schema(name = "ProductionOrderSummary", description = "Visão resumida de uma ordem de produção para listagens.")
public record ProductionOrderSummaryDTO(
        @Schema(description = "Identificador UUID da ordem.", example = "dc4b6f33-cb55-4ba4-8d9e-e2880c7b42f3")
        UUID id,
        @Schema(description = "Código do produto.", example = "12")
        Integer productCode,
        @Schema(description = "Nome do produto.", example = "Chapa de aço")
        String productName,
        @Schema(description = "Código da máquina.", example = "7")
        Integer machineCode,
        @Schema(description = "Nome da máquina.", example = "Centro de usinagem CNC")
        String machineName,
        @Schema(description = "Quantidade planejada.", example = "500")
        Integer targetQuantity,
        @Schema(description = "Quantidade produzida.", example = "200")
        Integer producedQuantity,
        @Schema(description = "Estado atual da ordem.", example = "IN_PROGRESS")
        ProductionOrderStatus status,
        @Schema(description = "Prazo da ordem.", example = "2026-11-15", nullable = true)
        LocalDate deadline
) {
}

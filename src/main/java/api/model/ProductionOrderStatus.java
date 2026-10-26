package api.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Define os estados possíveis para uma ordem de produção.
 */

@Schema(description = "Estado do ciclo de vida da ordem de produção.")
public enum ProductionOrderStatus {
    @Schema(description = "Ordem aguardando início.")
    PENDING,
    @Schema(description = "Produção em andamento.")
    IN_PROGRESS,
    @Schema(description = "Produção concluída.")
    COMPLETED,
    @Schema(description = "Ordem cancelada.")
    CANCELLED
}

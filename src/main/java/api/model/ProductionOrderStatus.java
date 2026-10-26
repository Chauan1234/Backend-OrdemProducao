// CAMADA: Model
// RESPONSABILIDADE: representar uma entidade ou enum do domínio.
// REGRAS: mantém o mapeamento persistente e os estados do domínio.
// ROTAS ATENDIDAS: não se aplica.
// SITUAÇÃO: implementada.
package api.model;

import io.swagger.v3.oas.annotations.media.Schema;

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

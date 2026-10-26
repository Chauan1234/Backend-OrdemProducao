// CAMADA: Model
// RESPONSABILIDADE: representar uma entidade ou enum do domínio.
// REGRAS: mantém o mapeamento persistente e os estados do domínio.
// ROTAS ATENDIDAS: não se aplica.
// SITUAÇÃO: implementada.
package api.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estado operacional da máquina.")
public enum MachineStatus {
    @Schema(description = "Máquina disponível para receber uma ordem.")
    AVAILABLE,
    @Schema(description = "Máquina em manutenção.")
    MAINTENANCE,
    @Schema(description = "Máquina inativa.")
    INACTIVE
}

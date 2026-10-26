// CAMADA: DTO
// RESPONSABILIDADE: transportar dados de entrada ou saída da API.
// REGRAS: define o contrato e as validações do payload.
// ROTAS ATENDIDAS: não se aplica.
// SITUAÇÃO: implementada.
package api.dto;

import api.model.MachineStatus;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UpdateMachineStatusRequest", description = "Novo estado a ser aplicado à máquina.")
public record UpdateMachineStatusRequestDTO(
        @NotNull
        @Schema(description = "Estado desejado: AVAILABLE, MAINTENANCE ou INACTIVE.", example = "MAINTENANCE")
        MachineStatus status
) {
}

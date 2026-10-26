package api.dto.machine;

import api.model.MachineStatus;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Representa a solicitação de alteração do estado de uma máquina.
 */

@Schema(name = "UpdateMachineStatusRequest", description = "Novo estado a ser aplicado à máquina.")
public record UpdateMachineStatusRequestDTO(
        @NotNull
        @Schema(description = "Estado desejado: AVAILABLE, MAINTENANCE ou INACTIVE.", example = "MAINTENANCE")
        MachineStatus status
) {
}

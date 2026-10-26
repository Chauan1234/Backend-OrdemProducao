package com.satc.edu.ordem_producao.machine.dto;

import com.satc.edu.ordem_producao.machine.entity.MachineStatus;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UpdateMachineStatusRequest", description = "Novo estado a ser aplicado à máquina.")
public record UpdateMachineStatusRequestDTO(
        @NotNull
        @Schema(description = "Estado desejado: AVAILABLE, MAINTENANCE ou INACTIVE.", example = "MAINTENANCE")
        MachineStatus status
) {
}

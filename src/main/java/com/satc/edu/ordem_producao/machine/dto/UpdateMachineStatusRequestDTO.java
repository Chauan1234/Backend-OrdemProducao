package com.satc.edu.ordem_producao.machine.dto;

import com.satc.edu.ordem_producao.machine.MachineStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateMachineStatusRequestDTO(
        @NotNull
        MachineStatus status
) {
}

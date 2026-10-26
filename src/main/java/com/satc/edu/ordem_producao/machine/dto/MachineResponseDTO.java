package com.satc.edu.ordem_producao.machine.dto;

import com.satc.edu.ordem_producao.machine.entity.MachineStatus;
import java.util.UUID;

public record MachineResponseDTO(
        UUID id,
        Integer code,
        String name,
        MachineStatus status
) {
}

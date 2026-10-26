package com.satc.edu.ordem_producao.machine.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MachineRequestDTO(
        @NotBlank
        @Size(max = 150)
        String name
) {
}

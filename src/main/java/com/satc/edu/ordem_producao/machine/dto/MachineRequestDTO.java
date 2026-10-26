package com.satc.edu.ordem_producao.machine.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "MachineRequest", description = "Dados recebidos para criar ou atualizar uma máquina.")
public record MachineRequestDTO(
        @NotBlank
        @Size(max = 150)
        @Schema(description = "Nome obrigatório da máquina, com até 150 caracteres.", example = "Centro de usinagem CNC")
        String name
) {
}

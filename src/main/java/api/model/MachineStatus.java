package api.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Define os estados operacionais possíveis para uma máquina.
 */

@Schema(description = "Estado operacional da máquina.")
public enum MachineStatus {
    @Schema(description = "Máquina disponível para receber uma ordem.")
    AVAILABLE,
    @Schema(description = "Máquina em manutenção.")
    MAINTENANCE,
    @Schema(description = "Máquina inativa.")
    INACTIVE
}

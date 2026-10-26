// CAMADA: DTO
// RESPONSABILIDADE: transportar dados de entrada ou saída da API.
// REGRAS: define o contrato e as validações do payload.
// ROTAS ATENDIDAS: não se aplica.
// SITUAÇÃO: implementada.
package api.dto;

import api.model.MachineStatus;
import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "MachineResponse", description = "Máquina retornada pela API.")
public record MachineResponseDTO(
        @Schema(description = "Identificador UUID da máquina.", example = "c37c6e28-9ab2-4b48-9d7d-59bb5152a013")
        UUID id,
        @Schema(description = "Código numérico gerado pelo sistema.", example = "7")
        Integer code,
        @Schema(description = "Nome da máquina.", example = "Centro de usinagem CNC")
        String name,
        @Schema(description = "Estado atual da máquina. Valores possíveis: AVAILABLE, MAINTENANCE e INACTIVE.", example = "AVAILABLE")
        MachineStatus status
) {
}

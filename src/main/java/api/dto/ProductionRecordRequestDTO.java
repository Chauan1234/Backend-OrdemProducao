// CAMADA: DTO
// RESPONSABILIDADE: transportar dados de entrada ou saída da API.
// REGRAS: define o contrato e as validações do payload.
// ROTAS ATENDIDAS: não se aplica.
// SITUAÇÃO: implementada.
package api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ProductionRecordRequest", description = "Dados para registrar produção em uma ordem em andamento.")
public record ProductionRecordRequestDTO(
        @NotNull
        @Positive
        @Schema(description = "Quantidade produzida neste registro, maior que zero.", example = "150", minimum = "1")
        Integer quantity,

        @Schema(description = "Observações opcionais sobre o registro.", example = "Lote produzido sem ocorrências.", nullable = true)
        String notes
) {
}

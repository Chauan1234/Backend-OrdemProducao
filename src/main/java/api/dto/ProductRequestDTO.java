// CAMADA: DTO
// RESPONSABILIDADE: transportar dados de entrada ou saída da API.
// REGRAS: define o contrato e as validações do payload.
// ROTAS ATENDIDAS: não se aplica.
// SITUAÇÃO: implementada.
package api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ProductRequest", description = "Dados recebidos para criar ou atualizar um produto.")
public record ProductRequestDTO(
        @NotBlank
        @Size(max = 150)
        @Schema(description = "Nome obrigatório do produto, com até 150 caracteres.", example = "Chapa de aço")
        String name,

        @Schema(description = "Descrição opcional do produto.", example = "Chapa laminada a frio", nullable = true)
        String description,

        @Schema(description = "Indica se o produto está ativo. Quando omitido na criação, será definido como ativo.", example = "true", nullable = true)
        Boolean active
) {
}

package api.dto.product;

import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Representa os dados de produto retornados pela API.
 */

@Schema(name = "ProductResponse", description = "Produto retornado pela API.")
public record ProductResponseDTO(
        @Schema(description = "Identificador UUID do produto.", example = "c37c6e28-9ab2-4b48-9d7d-59bb5152a013")
        UUID id,
        @Schema(description = "Código numérico gerado pelo sistema.", example = "12")
        Integer code,
        @Schema(description = "Nome do produto.", example = "Chapa de aço")
        String name,
        @Schema(description = "Descrição do produto.", example = "Chapa laminada a frio", nullable = true)
        String description,
        @Schema(description = "Indica se o produto está ativo.", example = "true")
        Boolean active
) {
}

package api.dto.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;

/**
 * Define o formato das respostas de erro da API.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "ErroDTO", description = "Formato de erro retornado pela API.")
public record ErroDTO(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        List<CampoErroDTO> campos) {
}

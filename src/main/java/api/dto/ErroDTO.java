package api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;

// CAMADA: DTO
// RESPONSABILIDADE: definir o formato único das respostas de erro da API.
// REGRAS: campos é omitido quando a falha não está associada a campos.
// ROTAS ATENDIDAS: todas as rotas HTTP.
// SITUAÇÃO: implementada.
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

package com.satc.edu.ordem_producao.common.exception;

import java.time.Instant;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ApiError", description = "Formato de erro retornado pela API.")
public record ApiError(
        @Schema(description = "Instante em que o erro ocorreu.", example = "2026-10-26T14:00:00Z") Instant timestamp,
        @Schema(description = "Código HTTP retornado.", example = "404") int status,
        @Schema(description = "Descrição resumida do erro HTTP.", example = "Not Found") String error,
        @Schema(description = "Detalhe do erro.", example = "Produto não encontrado.") String message,
        @Schema(description = "Caminho da requisição que falhou.", example = "/api/v1/product/abc") String path) {
}

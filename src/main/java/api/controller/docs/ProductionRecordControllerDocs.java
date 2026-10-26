package api.controller.docs;

import api.dto.ErroDTO;
import api.dto.ProductionRecordRequestDTO;
import api.dto.ProductionRecordResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

// CAMADA: Controller
// RESPONSABILIDADE: documentar as rotas de apontamentos no OpenAPI.
// REGRAS: mantém o contrato HTTP do ProductionRecordController.
// ROTAS ATENDIDAS: /api/v1/production-order/{id}/records.
// SITUAÇÃO: implementada.
public interface ProductionRecordControllerDocs {

    @Operation(summary = "Listar registros de produção",
            description = "Retorna os registros da ordem em páginas, ordenados pela data de registro.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Página de registros retornada."),
            @ApiResponse(responseCode = "404", description = "Ordem não encontrada.",
                    content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    ResponseEntity<Page<ProductionRecordResponseDTO>> findAll(
            @Parameter(description = "Identificador UUID da ordem.") UUID id,
            @ParameterObject Pageable pageable);

    @Operation(summary = "Registrar produção",
            description = "Registra uma quantidade produzida para uma ordem em andamento.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Registro de produção criado.",
                    content = @Content(schema = @Schema(implementation = ProductionRecordResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Quantidade inválida.",
                    content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "404", description = "Ordem não encontrada.",
                    content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "409", description = "A regra de produção foi violada.",
                    content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    ResponseEntity<ProductionRecordResponseDTO> create(
            @Parameter(description = "Identificador UUID da ordem.") UUID id,
            @Valid @RequestBody(description = "Quantidade produzida e observações opcionais.", required = true)
            ProductionRecordRequestDTO request);
}

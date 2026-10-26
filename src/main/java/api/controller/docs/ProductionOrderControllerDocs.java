package api.controller.docs;

import api.dto.error.ErroDTO;
import api.dto.productionorder.ProductionOrderRequestDTO;
import api.dto.productionorder.ProductionOrderResponseDTO;
import api.dto.productionorder.ProductionOrderSummaryDTO;
import api.model.ProductionOrderStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.UUID;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

/**
 * Documenta os endpoints de ordens de produção publicados no OpenAPI.
 */

public interface ProductionOrderControllerDocs {

    @Operation(
            summary = "Listar ordens de produção",
            description = "Retorna uma página de ordens resumidas. Permite filtrar por estado, produto, máquina e prazo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Página de ordens retornada. O conteúdo usa o schema ProductionOrderSummaryDTO."),
            @ApiResponse(
                    responseCode = "400", description = "Parâmetros inválidos.",
                    content = @Content(schema = @Schema(implementation = ErroDTO.class))
            )
    })
    ResponseEntity<Page<ProductionOrderSummaryDTO>> findAll(
            @Parameter(description = "Estado da ordem.") ProductionOrderStatus status,
            @Parameter(description = "UUID do produto associado.") UUID productId,
            @Parameter(description = "UUID da máquina associada.") UUID machineId,
            @Parameter(description = "Prazo da ordem.", example = "2026-11-15") LocalDate deadline,
            @ParameterObject @Parameter(description = "Parâmetros de paginação e ordenação.") Pageable pageable
    );

    @Operation(
            summary = "Buscar ordem por ID",
            description = "Retorna os detalhes da ordem, incluindo produto e máquina associados."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Ordem encontrada.",
                    content = @Content(schema = @Schema(implementation = ProductionOrderResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Ordem não encontrada.",
                    content = @Content(schema = @Schema(implementation = ErroDTO.class))
            )
    })
    ResponseEntity<ProductionOrderResponseDTO> findById(@Parameter(description = "Identificador UUID da ordem.") UUID id);

    @Operation(
            summary = "Criar ordem de produção",
            description = "Cria uma ordem pendente para um produto ativo e uma máquina disponível."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201", description = "Ordem criada.",
                    content = @Content(schema = @Schema(implementation = ProductionOrderResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400", description = "Dados inválidos.",
                    content = @Content(schema = @Schema(implementation = ErroDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Produto ou máquina não encontrado.",
                    content = @Content(schema = @Schema(implementation = ErroDTO.class))
            ),
            @ApiResponse(
                    responseCode = "409", description = "Produto ou máquina não está disponível para uma nova ordem.",
                    content = @Content(schema = @Schema(implementation = ErroDTO.class))
            )
    })
    ResponseEntity<ProductionOrderResponseDTO> create(
            @Valid @RequestBody(description = "Dados para criação da ordem.", required = true) ProductionOrderRequestDTO request
    );

    @Operation(
            summary = "Iniciar ordem",
            description = "Inicia uma ordem pendente se o produto continuar ativo e a máquina estiver disponível."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Ordem iniciada.",
                    content = @Content(schema = @Schema(implementation = ProductionOrderResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Ordem, produto ou máquina não encontrado.",
                    content = @Content(schema = @Schema(implementation = ErroDTO.class))
            ),
            @ApiResponse(
                    responseCode = "409", description = "A ordem não pode ser iniciada no estado ou condição atual.",
                    content = @Content(schema = @Schema(implementation = ErroDTO.class))
            )
    })
    ResponseEntity<ProductionOrderResponseDTO> start(@Parameter(description = "Identificador UUID da ordem.") UUID id);

    @Operation(
            summary = "Concluir ordem",
            description = "Conclui uma ordem em andamento quando a quantidade produzida alcança a quantidade planejada."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Ordem concluída.",
                    content = @Content(schema = @Schema(implementation = ProductionOrderResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Ordem não encontrada.",
                    content = @Content(schema = @Schema(implementation = ErroDTO.class))
            ),
            @ApiResponse(
                    responseCode = "409", description = "A ordem não está em andamento ou a produção está incompleta.",
                    content = @Content(schema = @Schema(implementation = ErroDTO.class))
            )
    })
    ResponseEntity<ProductionOrderResponseDTO> complete(@Parameter(description = "Identificador UUID da ordem.") UUID id);

    @Operation(
            summary = "Cancelar ordem",
            description = "Cancela uma ordem pendente ou em andamento e preserva o histórico de produção."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Ordem cancelada.",
                    content = @Content(schema = @Schema(implementation = ProductionOrderResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Ordem não encontrada.",
                    content = @Content(schema = @Schema(implementation = ErroDTO.class))
            ),
            @ApiResponse(
                    responseCode = "409", description = "A ordem não pode ser cancelada no estado atual.",
                    content = @Content(schema = @Schema(implementation = ErroDTO.class))
            )
    })
    ResponseEntity<ProductionOrderResponseDTO> cancel(@Parameter(description = "Identificador UUID da ordem.") UUID id);

}

package com.satc.edu.ordem_producao.productionorder.controller.docs;

import com.satc.edu.ordem_producao.common.exception.ApiError;
import com.satc.edu.ordem_producao.productionorder.dto.ProductionOrderRequestDTO;
import com.satc.edu.ordem_producao.productionorder.dto.ProductionOrderResponseDTO;
import com.satc.edu.ordem_producao.productionorder.dto.ProductionOrderSummaryDTO;
import com.satc.edu.ordem_producao.productionorder.entity.ProductionOrderStatus;
import com.satc.edu.ordem_producao.productionrecord.dto.ProductionRecordRequestDTO;
import com.satc.edu.ordem_producao.productionrecord.dto.ProductionRecordResponseDTO;
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

public interface ProductionOrderControllerDocs {

    @Operation(
            summary = "Listar ordens de produção",
            description = "Retorna uma página de ordens resumidas. Permite filtrar por estado, produto, máquina e prazo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Página de ordens retornada. O conteúdo usa o schema ProductionOrderSummaryDTO."),
            @ApiResponse(
                    responseCode = "400", description = "Parâmetros inválidos.",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
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
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            )
    })
    ResponseEntity<ProductionOrderResponseDTO> findById(@Parameter(description = "Identificador UUID da ordem.") UUID id);

    @Operation(
            summary = "Listar registros de produção",
            description = "Retorna os registros da ordem em páginas, ordenados pela data de registro."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Página de registros retornada. O conteúdo usa o schema ProductionRecordResponseDTO."),
            @ApiResponse(
                    responseCode = "404", description = "Ordem não encontrada.",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            )
    })
    ResponseEntity<Page<ProductionRecordResponseDTO>> findRecords(
            @Parameter(description = "Identificador UUID da ordem.") UUID id,
            @ParameterObject @Parameter(description = "Parâmetros de paginação e ordenação.") Pageable pageable
    );

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
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Produto ou máquina não encontrado.",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "409", description = "Produto ou máquina não está disponível para uma nova ordem.",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
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
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "409", description = "A ordem não pode ser iniciada no estado ou condição atual.",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
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
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "409", description = "A ordem não está em andamento ou a produção está incompleta.",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
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
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "409", description = "A ordem não pode ser cancelada no estado atual.",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            )
    })
    ResponseEntity<ProductionOrderResponseDTO> cancel(@Parameter(description = "Identificador UUID da ordem.") UUID id);

    @Operation(
            summary = "Registrar produção",
            description = "Registra uma quantidade produzida para uma ordem em andamento e atualiza sua quantidade produzida."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201", description = "Registro de produção criado.",
                    content = @Content(schema = @Schema(implementation = ProductionRecordResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400", description = "Quantidade ou observações inválidas.",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Ordem não encontrada.",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "409", description = "Ordem não está em andamento ou a quantidade ultrapassa o planejado.",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            )
    })
    ResponseEntity<ProductionRecordResponseDTO> createRecord(
            @Parameter(description = "Identificador UUID da ordem.") UUID id,
            @Valid @RequestBody(description = "Quantidade produzida e observações opcionais.", required = true) ProductionRecordRequestDTO request
    );

}

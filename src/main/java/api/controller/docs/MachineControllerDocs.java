// CAMADA: Controller
// RESPONSABILIDADE: documentar as rotas HTTP no OpenAPI.
// REGRAS: mantém o contrato dos Controllers correspondentes.
// ROTAS ATENDIDAS: as rotas documentadas pela interface.
// SITUAÇÃO: implementada.
package api.controller.docs;

import api.dto.ErroDTO;
import api.dto.MachineRequestDTO;
import api.dto.MachineResponseDTO;
import api.dto.UpdateMachineStatusRequestDTO;
import api.model.MachineStatus;
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

public interface MachineControllerDocs {

    @Operation(
            summary = "Listar máquinas",
            description = "Retorna uma página de máquinas, com filtro opcional por estado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Página de máquinas retornada. O conteúdo usa o schema MachineResponseDTO."),
            @ApiResponse(
                    responseCode = "400", description = "Parâmetros inválidos.",
                    content = @Content(schema = @Schema(implementation = ErroDTO.class))
            )
    })
    ResponseEntity<Page<MachineResponseDTO>> findAll(
            @Parameter(description = "Estado pelo qual filtrar as máquinas.") MachineStatus status,
            @ParameterObject @Parameter(description = "Parâmetros de paginação e ordenação.") Pageable pageable
    );

    @Operation(
            summary = "Buscar máquina por código",
            description = "Busca uma máquina pelo código gerado pelo sistema."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Máquina encontrada.",
                    content = @Content(schema = @Schema(implementation = MachineResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Máquina não encontrada.",
                    content = @Content(schema = @Schema(implementation = ErroDTO.class))
            )
    })
    ResponseEntity<MachineResponseDTO> findByCode(@Parameter(description = "Código da máquina.", example = "7") Integer code);

    @Operation(
            summary = "Buscar máquinas por nome",
            description = "Retorna uma página de máquinas cujo nome corresponde à busca."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Página de máquinas retornada. O conteúdo usa o schema MachineResponseDTO."),
            @ApiResponse(
                    responseCode = "400", description = "Parâmetros inválidos.",
                    content = @Content(schema = @Schema(implementation = ErroDTO.class))
            )
    })
    ResponseEntity<Page<MachineResponseDTO>> findByName(
            @Parameter(description = "Nome ou trecho do nome a pesquisar.", example = "CNC") String name,
            @ParameterObject @Parameter(description = "Parâmetros de paginação e ordenação.") Pageable pageable
    );

    @Operation(
            summary = "Buscar máquina por ID",
            description = "Retorna os dados de uma máquina pelo identificador UUID.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Máquina encontrada.",
                    content = @Content(schema = @Schema(implementation = MachineResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Máquina não encontrada.",
                    content = @Content(schema = @Schema(implementation = ErroDTO.class))
            )
    })
    ResponseEntity<MachineResponseDTO> findById(@Parameter(description = "Identificador UUID da máquina.") UUID id);

    @Operation(
            summary = "Cadastrar máquina",
            description = "Cria uma máquina com estado inicial AVAILABLE e código gerado pelo sistema."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201", description = "Máquina criada.",
                    content = @Content(schema = @Schema(implementation = MachineResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400", description = "Dados inválidos.",
                    content = @Content(schema = @Schema(implementation = ErroDTO.class))
            )
    })
    ResponseEntity<MachineResponseDTO> create(
            @Valid @RequestBody(description = "Dados para cadastro da máquina.", required = true) MachineRequestDTO request
    );

    @Operation(
            summary = "Atualizar máquina",
            description = "Atualiza o nome da máquina identificada."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Máquina atualizada.",
                    content = @Content(schema = @Schema(implementation = MachineResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400", description = "Dados inválidos.",
                    content = @Content(schema = @Schema(implementation = ErroDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Máquina não encontrada.",
                    content = @Content(schema = @Schema(implementation = ErroDTO.class))
            )
    })
    ResponseEntity<MachineResponseDTO> update(
            @Parameter(description = "Identificador UUID da máquina.") UUID id,
            @Valid @RequestBody(description = "Novos dados da máquina.", required = true) MachineRequestDTO request
    );

    @Operation(
            summary = "Alterar estado da máquina",
            description = "Atualiza o estado para AVAILABLE, MAINTENANCE ou INACTIVE. A mudança é rejeitada enquanto houver ordem em andamento."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Estado da máquina atualizado.",
                    content = @Content(schema = @Schema(implementation = MachineResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400", description = "Estado inválido.",
                    content = @Content(schema = @Schema(implementation = ErroDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Máquina não encontrada.",
                    content = @Content(schema = @Schema(implementation = ErroDTO.class))
            ),
            @ApiResponse(
                    responseCode = "409", description = "A máquina possui uma ordem em andamento.",
                    content = @Content(schema = @Schema(implementation = ErroDTO.class))
            )
    })
    ResponseEntity<MachineResponseDTO> updateStatus(
            @Parameter(description = "Identificador UUID da máquina.") UUID id,
            @Valid @RequestBody(description = "Novo estado da máquina.", required = true) UpdateMachineStatusRequestDTO request
    );

}

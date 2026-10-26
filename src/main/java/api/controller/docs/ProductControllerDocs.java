// CAMADA: Controller
// RESPONSABILIDADE: documentar as rotas HTTP no OpenAPI.
// REGRAS: mantém o contrato dos Controllers correspondentes.
// ROTAS ATENDIDAS: as rotas documentadas pela interface.
// SITUAÇÃO: implementada.
package api.controller.docs;

import api.dto.ErroDTO;
import api.dto.ProductRequestDTO;
import api.dto.ProductResponseDTO;
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

public interface ProductControllerDocs {

    @Operation(
            summary = "Listar produtos",
            description = "Retorna uma página de produtos, com filtro opcional pelo estado ativo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Página de produtos retornada. O conteúdo usa o schema ProductResponseDTO."),
            @ApiResponse(
                    responseCode = "400", description = "Parâmetros inválidos.",
                    content = @Content(schema = @Schema(implementation = ErroDTO.class))
            )
    })
    ResponseEntity<Page<ProductResponseDTO>> findAll(
            @Parameter(description = "Filtra pelo estado ativo do produto.", example = "true") Boolean active,
            @ParameterObject @Parameter(description = "Parâmetros de paginação e ordenação.") Pageable pageable
    );

    @Operation(
            summary = "Buscar produto por código",
            description = "Busca um produto pelo código gerado pelo sistema."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Produto encontrado.",
                    content = @Content(schema = @Schema(implementation = ProductResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Produto não encontrado.",
                    content = @Content(schema = @Schema(implementation = ErroDTO.class))
            )
    })
    ResponseEntity<ProductResponseDTO> findByCode(@Parameter(description = "Código do produto.", example = "12") Integer code);

    @Operation(
            summary = "Buscar produtos por nome",
            description = "Retorna uma página de produtos cujo nome corresponde à busca."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Página de produtos retornada. O conteúdo usa o schema ProductResponseDTO."),
            @ApiResponse(
                    responseCode = "400", description = "Parâmetros inválidos.",
                    content = @Content(schema = @Schema(implementation = ErroDTO.class))
            )
    })
    ResponseEntity<Page<ProductResponseDTO>> findByName(
            @Parameter(description = "Nome ou trecho do nome a pesquisar.", example = "Aço") String name,
            @ParameterObject @Parameter(description = "Parâmetros de paginação e ordenação.") Pageable pageable
    );

    @Operation(
            summary = "Cadastrar produto",
            description = "Cria um produto. O código é gerado automaticamente e o produto fica ativo por padrão."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201", description = "Produto criado.",
                    content = @Content(schema = @Schema(implementation = ProductResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400", description = "Dados inválidos.",
                    content = @Content(schema = @Schema(implementation = ErroDTO.class))
            )
    })
    ResponseEntity<ProductResponseDTO> create(
            @Valid @RequestBody(description = "Dados para cadastro do produto.", required = true) ProductRequestDTO request
    );

    @Operation(
            summary = "Buscar produto por ID",
            description = "Retorna os dados de um produto pelo identificador UUID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Produto encontrado.",
                    content = @Content(schema = @Schema(implementation = ProductResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Produto não encontrado.",
                    content = @Content(schema = @Schema(implementation = ErroDTO.class))
            )
    })
    ResponseEntity<ProductResponseDTO> findById(@Parameter(description = "Identificador UUID do produto.") UUID id);

    @Operation(
            summary = "Atualizar produto",
            description = "Atualiza os dados editáveis do produto identificado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Produto atualizado.",
                    content = @Content(schema = @Schema(implementation = ProductResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400", description = "Dados inválidos.",
                    content = @Content(schema = @Schema(implementation = ErroDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Produto não encontrado.",
                    content = @Content(schema = @Schema(implementation = ErroDTO.class))
            )
    })
    ResponseEntity<ProductResponseDTO> update(
            @Parameter(description = "Identificador UUID do produto.") UUID id,
            @Valid @RequestBody(description = "Novos dados do produto.", required = true) ProductRequestDTO request
    );

    @Operation(
            summary = "Desativar produto",
            description = "Desativa o produto sem alterar ordens de produção já existentes."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Produto desativado.",
                    content = @Content(schema = @Schema(implementation = ProductResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Produto não encontrado.",
                    content = @Content(schema = @Schema(implementation = ErroDTO.class))
            )
    })
    ResponseEntity<ProductResponseDTO> deactivate(@Parameter(description = "Identificador UUID do produto.") UUID id);

}

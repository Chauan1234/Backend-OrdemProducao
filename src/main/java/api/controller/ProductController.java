package api.controller;

import api.dto.product.ProductRequestDTO;
import api.dto.product.ProductResponseDTO;
import api.service.ProductService;
import api.controller.docs.ProductControllerDocs;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Expõe os endpoints HTTP para consulta e manutenção de produtos.
 */

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
@Tag(name = "Produtos")
public class ProductController implements ProductControllerDocs {
    private final ProductService service;

    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> findAll(@RequestParam(required = false) Boolean active,
                                                            @ParameterObject @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(service.findAll(active, pageable));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ProductResponseDTO> findByCode(@PathVariable Integer code) {
        return ResponseEntity.ok(service.findByCode(code));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Page<ProductResponseDTO>> findByName(@PathVariable String name,
                                                               @ParameterObject @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(service.findByName(name, pageable));
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@Valid @RequestBody ProductRequestDTO request) {
        ProductResponseDTO response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> update(@PathVariable UUID id,
                                                     @Valid @RequestBody ProductRequestDTO request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ProductResponseDTO> deactivate(@PathVariable UUID id) {
        return ResponseEntity.ok(service.deactivate(id));
    }

}

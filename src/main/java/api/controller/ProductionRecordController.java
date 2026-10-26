package api.controller;

import api.controller.docs.ProductionRecordControllerDocs;
import api.dto.ProductionRecordRequestDTO;
import api.dto.ProductionRecordResponseDTO;
import api.service.ProductionRecordService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// CAMADA: Controller
// RESPONSABILIDADE: expor as rotas HTTP dos apontamentos de produção.
// REGRAS: delega validação e regras de negócio ao Service.
// ROTAS ATENDIDAS: /api/v1/production-order/{id}/records.
// SITUAÇÃO: implementada.
@RestController
@RequestMapping("/api/v1/production-order/{id}/records")
@RequiredArgsConstructor
public class ProductionRecordController implements ProductionRecordControllerDocs {

    private final ProductionRecordService service;

    @GetMapping
    public ResponseEntity<Page<ProductionRecordResponseDTO>> findAll(
            @PathVariable UUID id,
            @ParameterObject @PageableDefault(size = 20, sort = "registeredAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(id, pageable));
    }

    @PostMapping
    public ResponseEntity<ProductionRecordResponseDTO> create(
            @PathVariable UUID id,
            @Valid @RequestBody ProductionRecordRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(id, request));
    }
}

package com.satc.edu.ordem_producao.productionorder.controller;

import com.satc.edu.ordem_producao.productionorder.dto.ProductionOrderRequestDTO;
import com.satc.edu.ordem_producao.productionorder.dto.ProductionOrderResponseDTO;
import com.satc.edu.ordem_producao.productionorder.dto.ProductionOrderSummaryDTO;
import com.satc.edu.ordem_producao.productionorder.entity.ProductionOrderStatus;
import com.satc.edu.ordem_producao.productionorder.service.ProductionOrderService;
import com.satc.edu.ordem_producao.productionrecord.dto.ProductionRecordRequestDTO;
import com.satc.edu.ordem_producao.productionrecord.dto.ProductionRecordResponseDTO;
import com.satc.edu.ordem_producao.productionrecord.service.ProductionRecordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/production-order")
@RequiredArgsConstructor
@Tag(name = "Ordens de produção")
public class ProductionOrderController {

    private final ProductionOrderService service;
    private final ProductionRecordService productionRecordService;

    @GetMapping
    public ResponseEntity<Page<ProductionOrderSummaryDTO>> findAll(@RequestParam(required = false) ProductionOrderStatus status,
                                                                   @RequestParam(required = false) UUID productId,
                                                                   @RequestParam(required = false) UUID machineId,
                                                                   @RequestParam(required = false) LocalDate deadline,
                                                                   @ParameterObject @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(service.findAll(status, productId, machineId, deadline, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductionOrderResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/{id}/records")
    public ResponseEntity<Page<ProductionRecordResponseDTO>> findRecords(@PathVariable UUID id,
                                                                         @ParameterObject @PageableDefault(size = 20, sort = "registeredAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(productionRecordService.findAll(id, pageable));
    }

    @PostMapping
    public ResponseEntity<ProductionOrderResponseDTO> create(@Valid @RequestBody ProductionOrderRequestDTO request) {
        var response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/start")
    public ResponseEntity<ProductionOrderResponseDTO> start(@PathVariable UUID id) {
        return ResponseEntity.ok(service.start(id));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<ProductionOrderResponseDTO> complete(@PathVariable UUID id) {
        return ResponseEntity.ok(service.complete(id));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ProductionOrderResponseDTO> cancel(@PathVariable UUID id) {
        return ResponseEntity.ok(service.cancel(id));
    }

    @PostMapping("/{id}/records")
    public ResponseEntity<ProductionRecordResponseDTO> createRecord(@PathVariable UUID id,
                                                                    @Valid @RequestBody ProductionRecordRequestDTO request) {
        var response = productionRecordService.create(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}

package com.satc.edu.ordem_producao.machine.controller;

import com.satc.edu.ordem_producao.machine.dto.MachineRequestDTO;
import com.satc.edu.ordem_producao.machine.dto.MachineResponseDTO;
import com.satc.edu.ordem_producao.machine.dto.UpdateMachineStatusRequestDTO;
import com.satc.edu.ordem_producao.machine.entity.MachineStatus;
import com.satc.edu.ordem_producao.machine.service.MachineService;
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

@RestController
@RequestMapping("/api/v1/machine")
@RequiredArgsConstructor
@Tag(name = "Máquinas")
public class MachineController {

    private final MachineService service;

    @GetMapping
    public ResponseEntity<Page<MachineResponseDTO>> findAll(@RequestParam(required = false) MachineStatus status,
                                                            @ParameterObject @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(service.findAll(status, pageable));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<MachineResponseDTO> findByCode(@PathVariable Integer code) {
        return ResponseEntity.ok(service.findByCode(code));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Page<MachineResponseDTO>> findByName(
            @PathVariable String name,
            @ParameterObject @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(service.findByName(name, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MachineResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<MachineResponseDTO> create(@Valid @RequestBody MachineRequestDTO request) {
        MachineResponseDTO response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MachineResponseDTO> update(@PathVariable UUID id,
                                                     @Valid @RequestBody MachineRequestDTO request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<MachineResponseDTO> updateStatus(@PathVariable UUID id,
                                                           @Valid @RequestBody UpdateMachineStatusRequestDTO request) {
        return ResponseEntity.ok(service.updateStatus(id, request));
    }

}

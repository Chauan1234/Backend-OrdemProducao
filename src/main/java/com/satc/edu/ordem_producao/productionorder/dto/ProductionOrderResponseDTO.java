package com.satc.edu.ordem_producao.productionorder.dto;

import com.satc.edu.ordem_producao.machine.dto.MachineResponseDTO;
import com.satc.edu.ordem_producao.product.dto.ProductResponseDTO;
import com.satc.edu.ordem_producao.productionorder.ProductionOrderStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductionOrderResponseDTO(
        UUID id,
        ProductResponseDTO product,
        MachineResponseDTO machine,
        Integer targetQuantity,
        Integer producedQuantity,
        ProductionOrderStatus status,
        LocalDateTime createdAt,
        LocalDateTime startedAt,
        LocalDateTime completedAt,
        LocalDate deadline
) {
}

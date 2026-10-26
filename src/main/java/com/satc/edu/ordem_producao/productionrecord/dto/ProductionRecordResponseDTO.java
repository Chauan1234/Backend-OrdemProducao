package com.satc.edu.ordem_producao.productionrecord.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProductionRecordResponseDTO(
        UUID id,
        UUID productionOrderId,

        Integer quantity,
        LocalDateTime registeredAt,

        String notes
) {
}

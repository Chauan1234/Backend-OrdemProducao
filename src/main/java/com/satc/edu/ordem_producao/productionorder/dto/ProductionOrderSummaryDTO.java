package com.satc.edu.ordem_producao.productionorder.dto;

import com.satc.edu.ordem_producao.productionorder.entity.ProductionOrderStatus;
import java.time.LocalDate;
import java.util.UUID;

public record ProductionOrderSummaryDTO(
        UUID id,
        Integer productCode,
        String productName,
        Integer machineCode,
        String machineName,
        Integer targetQuantity,
        Integer producedQuantity,
        ProductionOrderStatus status,
        LocalDate deadline
) {
}

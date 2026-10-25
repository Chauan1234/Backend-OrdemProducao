package com.satc.edu.ordem_producao.productionrecord;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductionRecordRepository extends JpaRepository<ProductionRecord, UUID> {
    Page<ProductionRecord> findByProductionOrder_IdOrderByRegisteredAtDesc(UUID productionOrderId, Pageable pageable);
}

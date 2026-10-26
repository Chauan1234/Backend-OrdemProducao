package com.satc.edu.ordem_producao.productionrecord.repository;

import com.satc.edu.ordem_producao.productionrecord.entity.ProductionRecord;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductionRecordRepository extends JpaRepository<ProductionRecord, UUID> {
    Page<ProductionRecord> findByProductionOrder_IdOrderByRegisteredAtDesc(UUID productionOrderId, Pageable pageable);
}

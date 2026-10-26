package api.repository;

import api.model.ProductionRecord;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Fornece operações de persistência e consulta de registros de produção.
 */

public interface ProductionRecordRepository extends JpaRepository<ProductionRecord, UUID> {
    Page<ProductionRecord> findByProductionOrder_IdOrderByRegisteredAtDesc(UUID productionOrderId, Pageable pageable);
}

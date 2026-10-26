package com.satc.edu.ordem_producao.productionorder;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ProductionOrderRepository extends JpaRepository<ProductionOrder, UUID>,
        JpaSpecificationExecutor<ProductionOrder> {

    boolean existsByMachine_IdAndStatus(UUID machineId, ProductionOrderStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select o from ProductionOrder o where o.id = :id")
    Optional<ProductionOrder> findByIdForUpdate(@Param("id") UUID id);
}

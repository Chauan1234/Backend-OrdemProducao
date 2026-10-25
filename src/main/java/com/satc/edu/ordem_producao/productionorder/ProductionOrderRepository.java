package com.satc.edu.ordem_producao.productionorder;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface ProductionOrderRepository extends JpaRepository<ProductionOrder, UUID> {

    @Query("""
            select o from ProductionOrder o
            where (:status is null or o.status = :status)
              and (:productId is null or o.product.id = :productId)
              and (:machineId is null or o.machine.id = :machineId)
              and (:deadline is null or o.deadline = :deadline)
            """)
    Page<ProductionOrder> search(
            @Param("status") ProductionOrderStatus status,
            @Param("productId") UUID productId,
            @Param("machineId") UUID machineId,
            @Param("deadline") LocalDate deadline,
            Pageable pageable
    );

    boolean existsByMachine_IdAndStatus(UUID machineId, ProductionOrderStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select o from ProductionOrder o where o.id = :id")
    Optional<ProductionOrder> findByIdForUpdate(@Param("id") UUID id);
}

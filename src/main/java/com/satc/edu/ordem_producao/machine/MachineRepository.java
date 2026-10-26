package com.satc.edu.ordem_producao.machine;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface MachineRepository extends JpaRepository<Machine, UUID> {
    Page<Machine> findByStatus(MachineStatus status, Pageable pageable);
    Optional<Machine> findByCode(Integer code);
    Page<Machine> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select m from Machine m where m.id = :id")
    Optional<Machine> findByIdForUpdate(@Param("id") UUID id);
}

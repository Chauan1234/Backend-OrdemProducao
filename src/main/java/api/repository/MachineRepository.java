package api.repository;

import api.model.Machine;
import api.model.MachineStatus;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Fornece operações de persistência e consulta de máquinas.
 */

public interface MachineRepository extends JpaRepository<Machine, UUID> {
    Page<Machine> findByStatus(MachineStatus status, Pageable pageable);
    Optional<Machine> findByCode(Integer code);
    Page<Machine> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select m from Machine m where m.id = :id")
    Optional<Machine> findByIdForUpdate(@Param("id") UUID id);
}

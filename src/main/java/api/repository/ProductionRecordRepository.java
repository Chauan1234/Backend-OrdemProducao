// CAMADA: Repository
// RESPONSABILIDADE: ler e gravar modelos no banco de dados.
// REGRAS: não aplica regras de negócio.
// ROTAS ATENDIDAS: não se aplica.
// SITUAÇÃO: implementada.
package api.repository;

import api.model.ProductionRecord;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductionRecordRepository extends JpaRepository<ProductionRecord, UUID> {
    Page<ProductionRecord> findByProductionOrder_IdOrderByRegisteredAtDesc(UUID productionOrderId, Pageable pageable);
}

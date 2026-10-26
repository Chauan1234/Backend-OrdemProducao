// CAMADA: Repository
// RESPONSABILIDADE: ler e gravar modelos no banco de dados.
// REGRAS: não aplica regras de negócio.
// ROTAS ATENDIDAS: não se aplica.
// SITUAÇÃO: implementada.
package api.repository;

import api.model.Product;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    Page<Product> findByActive(Boolean active, Pageable pageable);
    Optional<Product> findByCode(Integer code);
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Product p where p.id = :id")
    Optional<Product> findByIdForUpdate(@Param("id") UUID id);
}

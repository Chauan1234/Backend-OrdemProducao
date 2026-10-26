// CAMADA: Model
// RESPONSABILIDADE: representar uma entidade ou enum do domínio.
// REGRAS: mantém o mapeamento persistente e os estados do domínio.
// ROTAS ATENDIDAS: não se aplica.
// SITUAÇÃO: implementada.
package api.model;

import api.model.ProductionOrder;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "TB_PRODUCTION_RECORD")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class ProductionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "production_order_id", nullable = false)
    private ProductionOrder productionOrder;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private LocalDateTime registeredAt;

    @Column(columnDefinition = "TEXT")
    private String notes;

}

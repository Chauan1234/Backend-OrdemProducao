// CAMADA: Model
// RESPONSABILIDADE: representar uma entidade ou enum do domínio.
// REGRAS: mantém o mapeamento persistente e os estados do domínio.
// ROTAS ATENDIDAS: não se aplica.
// SITUAÇÃO: implementada.
package api.model;

import api.model.Machine;
import api.model.Product;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "TB_PRODUCTION_ORDER")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class ProductionOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "machine_id", nullable = false)
    private Machine machine;

    @Column(nullable = false)
    private Integer targetQuantity;

    @Column(nullable = false)
    private Integer producedQuantity;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(columnDefinition = "production_order_status")
    private ProductionOrderStatus status;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime startedAt;

    private LocalDateTime completedAt;

    private LocalDate deadline;

}

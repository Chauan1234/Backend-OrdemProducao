// CAMADA: Model
// RESPONSABILIDADE: representar uma entidade ou enum do domínio.
// REGRAS: mantém o mapeamento persistente e os estados do domínio.
// ROTAS ATENDIDAS: não se aplica.
// SITUAÇÃO: implementada.
package api.model;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

@Entity
@Table(name = "TB_PRODUCT")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Generated(event = EventType.INSERT)
    @Column(insertable = false, updatable = false, unique = true)
    private Integer code;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Boolean active;

}

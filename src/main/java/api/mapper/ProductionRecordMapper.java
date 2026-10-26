// CAMADA: Mapper
// RESPONSABILIDADE: converter DTOs e modelos de domínio.
// REGRAS: não aplica regras de negócio.
// ROTAS ATENDIDAS: não se aplica.
// SITUAÇÃO: implementada.
package api.mapper;

import api.model.ProductionOrder;
import api.dto.ProductionRecordRequestDTO;
import api.dto.ProductionRecordResponseDTO;
import api.model.ProductionRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductionRecordMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productionOrder", source = "order")
    @Mapping(target = "registeredAt", expression = "java(java.time.LocalDateTime.now())")
    ProductionRecord toProductionRecord(ProductionRecordRequestDTO request, ProductionOrder order);

    @Mapping(target = "productionOrderId", source = "productionOrder.id")
    ProductionRecordResponseDTO toResponse(ProductionRecord productionRecord);
}

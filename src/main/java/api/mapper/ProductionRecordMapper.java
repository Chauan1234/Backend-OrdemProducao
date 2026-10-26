package api.mapper;

import api.model.ProductionOrder;
import api.dto.productionrecord.ProductionRecordRequestDTO;
import api.dto.productionrecord.ProductionRecordResponseDTO;
import api.model.ProductionRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Converte entre registros de produção e seus DTOs.
 */

@Mapper(componentModel = "spring")
public interface ProductionRecordMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productionOrder", source = "order")
    @Mapping(target = "registeredAt", expression = "java(java.time.LocalDateTime.now())")
    ProductionRecord toProductionRecord(ProductionRecordRequestDTO request, ProductionOrder order);

    @Mapping(target = "productionOrderId", source = "productionOrder.id")
    ProductionRecordResponseDTO toResponse(ProductionRecord productionRecord);
}

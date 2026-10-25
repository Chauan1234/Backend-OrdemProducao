package com.satc.edu.ordem_producao.productionrecord;

import com.satc.edu.ordem_producao.productionrecord.dto.ProductionRecordRequestDTO;
import com.satc.edu.ordem_producao.productionrecord.dto.ProductionRecordResponseDTO;
import com.satc.edu.ordem_producao.productionorder.ProductionOrder;
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

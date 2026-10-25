package com.satc.edu.ordem_producao.productionrecord;

import com.satc.edu.ordem_producao.productionrecord.dto.ProductionRecordRequestDTO;
import com.satc.edu.ordem_producao.productionrecord.dto.ProductionRecordResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductionRecordMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productionOrder", ignore = true)
    @Mapping(target = "registeredAt", ignore = true)
    ProductionRecord toProductionRecord(ProductionRecordRequestDTO request);

    @Mapping(target = "productionOrderId", source = "productionOrder.id")
    ProductionRecordResponseDTO toResponse(ProductionRecord productionRecord);
}

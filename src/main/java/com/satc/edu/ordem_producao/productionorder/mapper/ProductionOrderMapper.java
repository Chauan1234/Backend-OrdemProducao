package com.satc.edu.ordem_producao.productionorder.mapper;

import com.satc.edu.ordem_producao.machine.entity.Machine;
import com.satc.edu.ordem_producao.machine.mapper.MachineMapper;
import com.satc.edu.ordem_producao.product.entity.Product;
import com.satc.edu.ordem_producao.product.mapper.ProductMapper;
import com.satc.edu.ordem_producao.productionorder.dto.ProductionOrderRequestDTO;
import com.satc.edu.ordem_producao.productionorder.dto.ProductionOrderResponseDTO;
import com.satc.edu.ordem_producao.productionorder.dto.ProductionOrderSummaryDTO;
import com.satc.edu.ordem_producao.productionorder.entity.ProductionOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductMapper.class, MachineMapper.class})
public interface ProductionOrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", source = "product")
    @Mapping(target = "machine", source = "machine")
    @Mapping(target = "producedQuantity", constant = "0")
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "startedAt", ignore = true)
    @Mapping(target = "completedAt", ignore = true)
    ProductionOrder toProductionOrder(ProductionOrderRequestDTO request, Product product, Machine machine);

    ProductionOrderResponseDTO toResponse(ProductionOrder productionOrder);

    @Mapping(target = "productCode", source = "product.code")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "machineCode", source = "machine.code")
    @Mapping(target = "machineName", source = "machine.name")
    ProductionOrderSummaryDTO toSummary(ProductionOrder productionOrder);
}

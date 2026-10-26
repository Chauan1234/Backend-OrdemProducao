package api.mapper;

import api.model.Machine;
import api.mapper.MachineMapper;
import api.model.Product;
import api.mapper.ProductMapper;
import api.dto.productionorder.ProductionOrderRequestDTO;
import api.dto.productionorder.ProductionOrderResponseDTO;
import api.dto.productionorder.ProductionOrderSummaryDTO;
import api.model.ProductionOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Converte entre ordens de produção e seus DTOs.
 */

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

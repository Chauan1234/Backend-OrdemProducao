package com.satc.edu.ordem_producao.productionrecord;

import com.satc.edu.ordem_producao.common.exception.BusinessConflictException;
import com.satc.edu.ordem_producao.common.exception.ResourceNotFoundException;
import com.satc.edu.ordem_producao.productionorder.ProductionOrderRepository;
import com.satc.edu.ordem_producao.productionorder.ProductionOrderStatus;
import com.satc.edu.ordem_producao.productionrecord.dto.ProductionRecordRequestDTO;
import com.satc.edu.ordem_producao.productionrecord.dto.ProductionRecordResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductionRecordService {

    private final ProductionRecordRepository repository;
    private final ProductionOrderRepository orderRepository;
    private final ProductionRecordMapper mapper;

    @Transactional
    public ProductionRecordResponseDTO create(UUID orderId, ProductionRecordRequestDTO request) {
        var order = orderRepository.findByIdForUpdate(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de produção", orderId));

        if (order.getStatus() != ProductionOrderStatus.IN_PROGRESS) {
            throw new BusinessConflictException("Só é possível registrar produção em uma ordem em andamento");
        }
        if (request.quantity() == null || request.quantity() <= 0) {
            throw new BusinessConflictException("A quantidade registrada deve ser positiva");
        }
        var produced = order.getProducedQuantity() + request.quantity();

        if (produced > order.getTargetQuantity()) {
            throw new BusinessConflictException("A quantidade registrada ultrapassa a quantidade planejada");
        }
        var record = mapper.toProductionRecord(request, order);
        order.setProducedQuantity(produced);
        repository.save(record);
        orderRepository.save(order);

        return mapper.toResponse(record);
    }

    @Transactional(readOnly = true)
    public Page<ProductionRecordResponseDTO> findAll(UUID orderId, Pageable pageable) {
        if (!orderRepository.existsById(orderId)) {
            throw new ResourceNotFoundException("Ordem de produção", orderId);
        }
        return repository.findByProductionOrder_IdOrderByRegisteredAtDesc(orderId, pageable).map(mapper::toResponse);
    }

}

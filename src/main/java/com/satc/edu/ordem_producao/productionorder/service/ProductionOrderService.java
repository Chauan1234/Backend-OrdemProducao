package com.satc.edu.ordem_producao.productionorder.service;

import com.satc.edu.ordem_producao.common.exception.BusinessConflictException;
import com.satc.edu.ordem_producao.common.exception.ResourceNotFoundException;
import com.satc.edu.ordem_producao.machine.entity.MachineStatus;
import com.satc.edu.ordem_producao.machine.repository.MachineRepository;
import com.satc.edu.ordem_producao.product.repository.ProductRepository;
import com.satc.edu.ordem_producao.productionorder.dto.ProductionOrderRequestDTO;
import com.satc.edu.ordem_producao.productionorder.dto.ProductionOrderResponseDTO;
import com.satc.edu.ordem_producao.productionorder.dto.ProductionOrderSummaryDTO;
import com.satc.edu.ordem_producao.productionorder.entity.ProductionOrder;
import com.satc.edu.ordem_producao.productionorder.entity.ProductionOrderStatus;
import com.satc.edu.ordem_producao.productionorder.mapper.ProductionOrderMapper;
import com.satc.edu.ordem_producao.productionorder.repository.ProductionOrderRepository;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductionOrderService {

    private final ProductionOrderRepository repository;
    private final ProductRepository productRepository;
    private final MachineRepository machineRepository;
    private final ProductionOrderMapper mapper;

    @Transactional
    public ProductionOrderResponseDTO create(ProductionOrderRequestDTO request) {
        validateDeadline(request.deadline());

        var product = productRepository.findByIdForUpdate(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto", request.productId()));
        var machine = machineRepository.findByIdForUpdate(request.machineId())
                .orElseThrow(() -> new ResourceNotFoundException("Máquina", request.machineId()));
        if (!Boolean.TRUE.equals(product.getActive())) {
            throw new BusinessConflictException("Não é possível criar uma ordem para um produto inativo");
        }
        if (machine.getStatus() != MachineStatus.AVAILABLE) {
            throw new BusinessConflictException("A máquina precisa estar disponível para receber uma ordem");
        }

        var order = mapper.toProductionOrder(request, product, machine);
        return mapper.toResponse(repository.save(order));
    }

    @Transactional(readOnly = true)
    public Page<ProductionOrderSummaryDTO> findAll(ProductionOrderStatus status, UUID productId,
                                                   UUID machineId, LocalDate deadline, Pageable pageable) {
        Specification<ProductionOrder> specification = (root, query, criteriaBuilder) -> {
            var predicates = new ArrayList<Predicate>();
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            if (productId != null) {
                predicates.add(criteriaBuilder.equal(root.get("product").get("id"), productId));
            }
            if (machineId != null) {
                predicates.add(criteriaBuilder.equal(root.get("machine").get("id"), machineId));
            }
            if (deadline != null) {
                predicates.add(criteriaBuilder.equal(root.get("deadline"), deadline));
            }
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
        return repository.findAll(specification, pageable).map(mapper::toSummary);
    }

    @Transactional(readOnly = true)
    public ProductionOrderResponseDTO findById(UUID id) {
        return mapper.toResponse(getById(id));
    }

    @Transactional
    public ProductionOrderResponseDTO start(UUID id) {
        var order = getForUpdate(id);
        var productId = order.getProduct().getId();
        var machineId = order.getMachine().getId();

        var product = productRepository.findByIdForUpdate(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", productId));
        var machine = machineRepository.findByIdForUpdate(machineId)
                .orElseThrow(() -> new ResourceNotFoundException("Máquina", machineId));

        requireStatus(order, ProductionOrderStatus.PENDING, "Apenas ordens pendentes podem ser iniciadas");

        if (!Boolean.TRUE.equals(product.getActive())) {
            throw new BusinessConflictException("O produto da ordem está inativo");
        }
        if (machine.getStatus() != MachineStatus.AVAILABLE) {
            throw new BusinessConflictException("A máquina da ordem não está disponível");
        }
        if (repository.existsByMachine_IdAndStatus(machineId, ProductionOrderStatus.IN_PROGRESS)) {
            throw new BusinessConflictException("A máquina já está executando outra ordem");
        }
        order.setStatus(ProductionOrderStatus.IN_PROGRESS);
        order.setStartedAt(LocalDateTime.now());
        return mapper.toResponse(repository.save(order));
    }

    @Transactional
    public ProductionOrderResponseDTO complete(UUID id) {
        var order = getForUpdate(id);
        requireStatus(order, ProductionOrderStatus.IN_PROGRESS, "Apenas ordens em andamento podem ser concluídas");
        if (!order.getTargetQuantity().equals(order.getProducedQuantity())) {
            throw new BusinessConflictException("A quantidade produzida deve atingir a quantidade planejada");
        }
        order.setStatus(ProductionOrderStatus.COMPLETED);
        order.setCompletedAt(LocalDateTime.now());
        return mapper.toResponse(repository.save(order));
    }

    @Transactional
    public ProductionOrderResponseDTO cancel(UUID id) {
        var order = getForUpdate(id);
        if (order.getStatus() != ProductionOrderStatus.PENDING && order.getStatus() != ProductionOrderStatus.IN_PROGRESS) {
            throw new BusinessConflictException("Apenas ordens pendentes ou em andamento podem ser canceladas");
        }
        order.setStatus(ProductionOrderStatus.CANCELLED);
        return mapper.toResponse(repository.save(order));
    }

    private ProductionOrder getById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ordem de produção", id));
    }

    private ProductionOrder getForUpdate(UUID id) {
        return repository.findByIdForUpdate(id).orElseThrow(() -> new ResourceNotFoundException("Ordem de produção", id));
    }

    private void validateDeadline(LocalDate deadline) {
        if (deadline != null && deadline.isBefore(LocalDate.now())) {
            throw new BusinessConflictException("O prazo não pode ser anterior à data atual");
        }
    }

    private void requireStatus(ProductionOrder order, ProductionOrderStatus expected, String message) {
        if (order.getStatus() != expected) {
            throw new BusinessConflictException(message);
        }
    }

}

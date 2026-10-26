package api.service;

import api.service.exception.MaquinaNaoEncontradaException;
import api.service.exception.OrdemDeProducaoNaoEncontradaException;
import api.service.exception.ProdutoNaoEncontradoException;
import api.service.exception.RegraDeNegocioException;

import api.model.MachineStatus;
import api.repository.MachineRepository;
import api.repository.ProductRepository;
import api.dto.productionorder.ProductionOrderRequestDTO;
import api.dto.productionorder.ProductionOrderResponseDTO;
import api.dto.productionorder.ProductionOrderSummaryDTO;
import api.model.ProductionOrder;
import api.model.ProductionOrderStatus;
import api.mapper.ProductionOrderMapper;
import api.repository.ProductionOrderRepository;
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

/**
 * Aplica as regras de negócio do ciclo de vida das ordens de produção.
 */

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
                .orElseThrow(() -> new ProdutoNaoEncontradoException(request.productId()));
        var machine = machineRepository.findByIdForUpdate(request.machineId())
                .orElseThrow(() -> new MaquinaNaoEncontradaException(request.machineId()));
        if (!Boolean.TRUE.equals(product.getActive())) {
            throw new RegraDeNegocioException("Não é possível criar uma ordem para um produto inativo");
        }
        if (machine.getStatus() != MachineStatus.AVAILABLE) {
            throw new RegraDeNegocioException("A máquina precisa estar disponível para receber uma ordem");
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
                .orElseThrow(() -> new ProdutoNaoEncontradoException(productId));
        var machine = machineRepository.findByIdForUpdate(machineId)
                .orElseThrow(() -> new MaquinaNaoEncontradaException(machineId));

        requireStatus(order, ProductionOrderStatus.PENDING, "Apenas ordens pendentes podem ser iniciadas");

        if (!Boolean.TRUE.equals(product.getActive())) {
            throw new RegraDeNegocioException("O produto da ordem está inativo");
        }
        if (machine.getStatus() != MachineStatus.AVAILABLE) {
            throw new RegraDeNegocioException("A máquina da ordem não está disponível");
        }
        if (repository.existsByMachine_IdAndStatus(machineId, ProductionOrderStatus.IN_PROGRESS)) {
            throw new RegraDeNegocioException("A máquina já está executando outra ordem");
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
            throw new RegraDeNegocioException("A quantidade produzida deve atingir a quantidade planejada");
        }
        order.setStatus(ProductionOrderStatus.COMPLETED);
        order.setCompletedAt(LocalDateTime.now());
        return mapper.toResponse(repository.save(order));
    }

    @Transactional
    public ProductionOrderResponseDTO cancel(UUID id) {
        var order = getForUpdate(id);
        if (order.getStatus() != ProductionOrderStatus.PENDING && order.getStatus() != ProductionOrderStatus.IN_PROGRESS) {
            throw new RegraDeNegocioException("Apenas ordens pendentes ou em andamento podem ser canceladas");
        }
        order.setStatus(ProductionOrderStatus.CANCELLED);
        return mapper.toResponse(repository.save(order));
    }

    private ProductionOrder getById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new OrdemDeProducaoNaoEncontradaException(id));
    }

    private ProductionOrder getForUpdate(UUID id) {
        return repository.findByIdForUpdate(id).orElseThrow(() -> new OrdemDeProducaoNaoEncontradaException(id));
    }

    private void validateDeadline(LocalDate deadline) {
        if (deadline != null && deadline.isBefore(LocalDate.now())) {
            throw new RegraDeNegocioException("O prazo não pode ser anterior à data atual");
        }
    }

    private void requireStatus(ProductionOrder order, ProductionOrderStatus expected, String message) {
        if (order.getStatus() != expected) {
            throw new RegraDeNegocioException(message);
        }
    }

}

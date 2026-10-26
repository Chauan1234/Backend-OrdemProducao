// CAMADA: Service
// RESPONSABILIDADE: aplicar regras de negócio e coordenar as camadas.
// REGRAS: centraliza as decisões do domínio.
// ROTAS ATENDIDAS: as rotas que utilizam este Service.
// SITUAÇÃO: implementada.
package api.service;

import api.model.ProductionOrderStatus;
import api.repository.ProductionOrderRepository;
import api.dto.ProductionRecordRequestDTO;
import api.dto.ProductionRecordResponseDTO;
import api.mapper.ProductionRecordMapper;
import api.repository.ProductionRecordRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductionRecordService {

    private final ProductionRecordRepository repository;
    private final ProductionOrderRepository orderRepository;
    private final ProductionRecordMapper mapper;

    @Transactional
    public ProductionRecordResponseDTO create(UUID orderId, ProductionRecordRequestDTO request) {
        var order = orderRepository.findByIdForUpdate(orderId)
                .orElseThrow(() -> new OrdemDeProducaoNaoEncontradaException(orderId));

        if (order.getStatus() != ProductionOrderStatus.IN_PROGRESS) {
            throw new RegraDeNegocioException("Só é possível registrar produção em uma ordem em andamento");
        }
        if (request.quantity() == null || request.quantity() <= 0) {
            throw new RegraDeNegocioException("A quantidade registrada deve ser positiva");
        }
        long produced = (long) order.getProducedQuantity() + request.quantity();

        if (produced < 0 || produced > order.getTargetQuantity()) {
            throw new RegraDeNegocioException("A quantidade registrada ultrapassa a quantidade planejada");
        }
        var record = mapper.toProductionRecord(request, order);
        order.setProducedQuantity((int) produced);
        repository.save(record);
        orderRepository.save(order);

        return mapper.toResponse(record);
    }

    @Transactional(readOnly = true)
    public Page<ProductionRecordResponseDTO> findAll(UUID orderId, Pageable pageable) {
        if (!orderRepository.existsById(orderId)) {
            throw new OrdemDeProducaoNaoEncontradaException(orderId);
        }
        return repository.findByProductionOrder_IdOrderByRegisteredAtDesc(orderId, pageable).map(mapper::toResponse);
    }

}

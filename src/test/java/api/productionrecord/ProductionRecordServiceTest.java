package api.productionrecord;

import api.service.exception.RegraDeNegocioException;
import api.service.exception.OrdemDeProducaoNaoEncontradaException;
import api.model.ProductionOrder;
import api.model.ProductionOrderStatus;
import api.repository.ProductionOrderRepository;
import api.dto.productionrecord.ProductionRecordRequestDTO;
import api.dto.productionrecord.ProductionRecordResponseDTO;
import api.model.ProductionRecord;
import api.mapper.ProductionRecordMapper;
import api.repository.ProductionRecordRepository;
import api.service.ProductionRecordService;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductionRecordServiceTest {

    private ProductionRecordRepository recordRepository;
    private ProductionOrderRepository orderRepository;
    private ProductionRecordMapper mapper;
    private ProductionRecordService service;

    @BeforeEach
    void setUp() {
        recordRepository = mock(ProductionRecordRepository.class);
        orderRepository = mock(ProductionOrderRepository.class);
        mapper = mock(ProductionRecordMapper.class);
        service = new ProductionRecordService(recordRepository, orderRepository, mapper);
    }

    @Test
    void createRejectsOverflowWithoutSavingRecordOrChangingOrder() {
        var id = UUID.randomUUID();
        var order = order(id, 1, 100, ProductionOrderStatus.IN_PROGRESS);
        when(orderRepository.findByIdForUpdate(id)).thenReturn(Optional.of(order));

        assertThrows(RegraDeNegocioException.class,
                () -> service.create(id, new ProductionRecordRequestDTO(Integer.MAX_VALUE, null)));

        assertEquals(1, order.getProducedQuantity());
        verifyNoInteractions(mapper, recordRepository);
        verify(orderRepository, never()).save(any());
    }

    @Test
    void createAcceptsQuantityThatExactlyReachesTarget() {
        var id = UUID.randomUUID();
        var order = order(id, 40, 100, ProductionOrderStatus.IN_PROGRESS);
        var request = new ProductionRecordRequestDTO(60, "Final batch");
        var record = ProductionRecord.builder()
                .productionOrder(order)
                .quantity(60)
                .registeredAt(LocalDateTime.now())
                .notes("Final batch")
                .build();
        var response = new ProductionRecordResponseDTO(UUID.randomUUID(), id, 60,
                record.getRegisteredAt(), "Final batch");
        when(orderRepository.findByIdForUpdate(id)).thenReturn(Optional.of(order));
        when(mapper.toProductionRecord(request, order)).thenReturn(record);
        when(mapper.toResponse(record)).thenReturn(response);

        var actual = service.create(id, request);

        assertSame(response, actual);
        assertEquals(100, order.getProducedQuantity());
        verify(recordRepository).save(record);
        verify(orderRepository).save(order);
    }

    @Test
    void createRejectsQuantityAboveTargetWithoutSaving() {
        var id = UUID.randomUUID();
        var order = order(id, 40, 100, ProductionOrderStatus.IN_PROGRESS);
        when(orderRepository.findByIdForUpdate(id)).thenReturn(Optional.of(order));

        assertThrows(RegraDeNegocioException.class,
                () -> service.create(id, new ProductionRecordRequestDTO(61, null)));

        assertEquals(40, order.getProducedQuantity());
        verifyNoInteractions(mapper, recordRepository);
        verify(orderRepository, never()).save(any());
    }

    @Test
    void createRejectsOrderThatIsNotInProgress() {
        var id = UUID.randomUUID();
        var order = order(id, 0, 100, ProductionOrderStatus.PENDING);
        when(orderRepository.findByIdForUpdate(id)).thenReturn(Optional.of(order));

        assertThrows(RegraDeNegocioException.class,
                () -> service.create(id, new ProductionRecordRequestDTO(10, null)));

        verifyNoInteractions(mapper, recordRepository);
        verify(orderRepository, never()).save(any());
    }

    @Test
    void createThrowsNotFoundWhenOrderDoesNotExist() {
        var id = UUID.randomUUID();
        when(orderRepository.findByIdForUpdate(id)).thenReturn(Optional.empty());

        var exception = assertThrows(OrdemDeProducaoNaoEncontradaException.class,
                () -> service.create(id, new ProductionRecordRequestDTO(10, null)));
        assertEquals("Ordem de produção não encontrado: " + id, exception.getMessage());

        verifyNoInteractions(mapper, recordRepository);
        verify(orderRepository, never()).save(any());
    }

    private ProductionOrder order(UUID id, int produced, int target, ProductionOrderStatus status) {
        return ProductionOrder.builder()
                .id(id)
                .producedQuantity(produced)
                .targetQuantity(target)
                .status(status)
                .build();
    }
}

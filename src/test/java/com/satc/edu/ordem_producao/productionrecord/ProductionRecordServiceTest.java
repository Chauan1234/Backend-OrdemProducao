package com.satc.edu.ordem_producao.productionrecord;

import com.satc.edu.ordem_producao.common.exception.BusinessConflictException;
import com.satc.edu.ordem_producao.common.exception.ResourceNotFoundException;
import com.satc.edu.ordem_producao.productionorder.entity.ProductionOrder;
import com.satc.edu.ordem_producao.productionorder.entity.ProductionOrderStatus;
import com.satc.edu.ordem_producao.productionorder.repository.ProductionOrderRepository;
import com.satc.edu.ordem_producao.productionrecord.dto.ProductionRecordRequestDTO;
import com.satc.edu.ordem_producao.productionrecord.dto.ProductionRecordResponseDTO;
import com.satc.edu.ordem_producao.productionrecord.entity.ProductionRecord;
import com.satc.edu.ordem_producao.productionrecord.mapper.ProductionRecordMapper;
import com.satc.edu.ordem_producao.productionrecord.repository.ProductionRecordRepository;
import com.satc.edu.ordem_producao.productionrecord.service.ProductionRecordService;
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

        assertThrows(BusinessConflictException.class,
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

        assertThrows(BusinessConflictException.class,
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

        assertThrows(BusinessConflictException.class,
                () -> service.create(id, new ProductionRecordRequestDTO(10, null)));

        verifyNoInteractions(mapper, recordRepository);
        verify(orderRepository, never()).save(any());
    }

    @Test
    void createThrowsNotFoundWhenOrderDoesNotExist() {
        var id = UUID.randomUUID();
        when(orderRepository.findByIdForUpdate(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.create(id, new ProductionRecordRequestDTO(10, null)));

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

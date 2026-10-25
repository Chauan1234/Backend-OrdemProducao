package com.satc.edu.ordem_producao.machine;

import com.satc.edu.ordem_producao.common.exception.BusinessConflictException;
import com.satc.edu.ordem_producao.common.exception.ResourceNotFoundException;
import com.satc.edu.ordem_producao.machine.dto.UpdateMachineStatusRequestDTO;
import com.satc.edu.ordem_producao.productionorder.ProductionOrderRepository;
import com.satc.edu.ordem_producao.productionorder.ProductionOrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MachineServiceTest {

    private MachineRepository machineRepository;
    private ProductionOrderRepository productionOrderRepository;
    private MachineService service;

    @BeforeEach
    void setUp() {
        machineRepository = mock(MachineRepository.class);
        productionOrderRepository = mock(ProductionOrderRepository.class);
        service = new MachineService(
                machineRepository,
                productionOrderRepository,
                Mappers.getMapper(MachineMapper.class)
        );
    }

    @Test
    void updateStatusRejectsStatusChangeWhenMachineHasInProgressOrder() {
        var id = UUID.randomUUID();
        var machine = machine(id, MachineStatus.AVAILABLE);
        when(machineRepository.findByIdForUpdate(id)).thenReturn(Optional.of(machine));
        when(productionOrderRepository.existsByMachine_IdAndStatus(id, ProductionOrderStatus.IN_PROGRESS))
                .thenReturn(true);

        assertThrows(BusinessConflictException.class,
                () -> service.updateStatus(id, new UpdateMachineStatusRequestDTO(MachineStatus.MAINTENANCE)));

        verify(machineRepository).findByIdForUpdate(id);
        verify(machineRepository, never()).findById(id);
        verify(machineRepository, never()).save(any());
    }

    @Test
    void updateStatusChangesMachineWhenThereIsNoInProgressOrder() {
        var id = UUID.randomUUID();
        var machine = machine(id, MachineStatus.AVAILABLE);
        when(machineRepository.findByIdForUpdate(id)).thenReturn(Optional.of(machine));
        when(productionOrderRepository.existsByMachine_IdAndStatus(id, ProductionOrderStatus.IN_PROGRESS))
                .thenReturn(false);
        when(machineRepository.save(any(Machine.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.updateStatus(id, new UpdateMachineStatusRequestDTO(MachineStatus.MAINTENANCE));

        assertEquals(MachineStatus.MAINTENANCE, response.status());
        assertEquals(MachineStatus.MAINTENANCE, machine.getStatus());
        verify(machineRepository).findByIdForUpdate(id);
        verify(machineRepository, never()).findById(id);
        verify(machineRepository).save(machine);
    }

    @Test
    void updateStatusIsNoOpWhenRequestedStatusMatchesDuringInProgressOrder() {
        var id = UUID.randomUUID();
        var machine = machine(id, MachineStatus.AVAILABLE);
        when(machineRepository.findByIdForUpdate(id)).thenReturn(Optional.of(machine));
        when(productionOrderRepository.existsByMachine_IdAndStatus(id, ProductionOrderStatus.IN_PROGRESS))
                .thenReturn(true);

        var response = service.updateStatus(id, new UpdateMachineStatusRequestDTO(MachineStatus.AVAILABLE));

        assertEquals(MachineStatus.AVAILABLE, response.status());
        verify(machineRepository).findByIdForUpdate(id);
        verify(machineRepository, never()).findById(id);
        verify(machineRepository, never()).save(any());
    }

    @Test
    void updateStatusThrowsNotFoundWhenMachineDoesNotExist() {
        var id = UUID.randomUUID();
        when(machineRepository.findByIdForUpdate(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.updateStatus(id, new UpdateMachineStatusRequestDTO(MachineStatus.MAINTENANCE)));

        verify(machineRepository).findByIdForUpdate(id);
        verify(machineRepository, never()).findById(id);
        verifyNoInteractions(productionOrderRepository);
        verify(machineRepository, never()).save(any());
    }

    private Machine machine(UUID id, MachineStatus status) {
        return Machine.builder()
                .id(id)
                .code(1)
                .name("Machine 1")
                .status(status)
                .build();
    }
}

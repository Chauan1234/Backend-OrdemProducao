package com.satc.edu.ordem_producao.machine.service;

import com.satc.edu.ordem_producao.common.exception.BusinessConflictException;
import com.satc.edu.ordem_producao.common.exception.ResourceNotFoundException;
import com.satc.edu.ordem_producao.machine.dto.MachineRequestDTO;
import com.satc.edu.ordem_producao.machine.dto.MachineResponseDTO;
import com.satc.edu.ordem_producao.machine.dto.UpdateMachineStatusRequestDTO;
import com.satc.edu.ordem_producao.machine.entity.Machine;
import com.satc.edu.ordem_producao.machine.entity.MachineStatus;
import com.satc.edu.ordem_producao.machine.mapper.MachineMapper;
import com.satc.edu.ordem_producao.machine.repository.MachineRepository;
import com.satc.edu.ordem_producao.productionorder.entity.ProductionOrderStatus;
import com.satc.edu.ordem_producao.productionorder.repository.ProductionOrderRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MachineService {

    private final MachineRepository repository;
    private final ProductionOrderRepository productionOrderRepository;
    private final MachineMapper mapper;

    @Transactional
    public MachineResponseDTO create(MachineRequestDTO request) {
        var machine = mapper.toMachine(request);
        machine.setStatus(MachineStatus.AVAILABLE);
        return mapper.toResponse(repository.saveAndFlush(machine));
    }

    @Transactional(readOnly = true)
    public Page<MachineResponseDTO> findAll(MachineStatus status, Pageable pageable) {
        var machines = status == null
                ? repository.findAll(pageable)
                : repository.findByStatus(status, pageable);
        return machines.map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public MachineResponseDTO findById(UUID id) {
        return mapper.toResponse(getById(id));
    }

    @Transactional(readOnly = true)
    public MachineResponseDTO findByCode(Integer code) {
        var machine = repository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Máquina", "código " + code));
        return mapper.toResponse(machine);
    }

    @Transactional(readOnly = true)
    public Page<MachineResponseDTO> findByName(String name, Pageable pageable) {
        return repository.findByNameContainingIgnoreCase(name.trim(), pageable).map(mapper::toResponse);
    }

    @Transactional
    public MachineResponseDTO update(UUID id, MachineRequestDTO request) {
        var machine = getById(id);
        mapper.update(request, machine);
        return mapper.toResponse(repository.save(machine));
    }

    @Transactional
    public MachineResponseDTO updateStatus(UUID id, UpdateMachineStatusRequestDTO request) {
        var machine = repository.findByIdForUpdate(id)
                .orElseThrow(() -> new ResourceNotFoundException("Máquina", id));
        var hasProductionOrder = productionOrderRepository.existsByMachine_IdAndStatus(id, ProductionOrderStatus.IN_PROGRESS);

        if (hasProductionOrder && request.status() == machine.getStatus()) {
            return mapper.toResponse(machine);
        }
        if (hasProductionOrder) {
            throw new BusinessConflictException("A máquina não pode mudar de estado enquanto executa uma ordem");
        }
        mapper.updateStatus(request, machine);
        return mapper.toResponse(repository.save(machine));
    }

    private Machine getById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Máquina", id));
    }

}

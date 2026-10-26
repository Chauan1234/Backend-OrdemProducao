// CAMADA: Mapper
// RESPONSABILIDADE: converter DTOs e modelos de domínio.
// REGRAS: não aplica regras de negócio.
// ROTAS ATENDIDAS: não se aplica.
// SITUAÇÃO: implementada.
package api.mapper;

import api.dto.MachineRequestDTO;
import api.dto.MachineResponseDTO;
import api.dto.UpdateMachineStatusRequestDTO;
import api.model.Machine;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface MachineMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "status", ignore = true)
    Machine toMachine(MachineRequestDTO request);

    MachineResponseDTO toResponse(Machine machine);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "status", ignore = true)
    void update(MachineRequestDTO request, @MappingTarget Machine machine);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "name", ignore = true)
    void updateStatus(UpdateMachineStatusRequestDTO request, @MappingTarget Machine machine);
}

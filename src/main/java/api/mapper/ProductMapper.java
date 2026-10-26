// CAMADA: Mapper
// RESPONSABILIDADE: converter DTOs e modelos de domínio.
// REGRAS: não aplica regras de negócio.
// ROTAS ATENDIDAS: não se aplica.
// SITUAÇÃO: implementada.
package api.mapper;

import api.dto.ProductRequestDTO;
import api.dto.ProductResponseDTO;
import api.model.Product;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    Product toProduct(ProductRequestDTO request);

    ProductResponseDTO toResponse(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    void update(ProductRequestDTO request, @MappingTarget Product product);
}

package com.satc.edu.ordem_producao.product.mapper;

import com.satc.edu.ordem_producao.product.dto.ProductRequestDTO;
import com.satc.edu.ordem_producao.product.dto.ProductResponseDTO;
import com.satc.edu.ordem_producao.product.entity.Product;
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

package com.satc.edu.ordem_producao.product;

import com.satc.edu.ordem_producao.common.exception.ResourceNotFoundException;
import com.satc.edu.ordem_producao.product.dto.ProductRequestDTO;
import com.satc.edu.ordem_producao.product.dto.ProductResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    @Transactional
    public ProductResponseDTO create(ProductRequestDTO request) {
        var product = mapper.toProduct(request);

        if (product.getActive() == null) {
            product.setActive(true);
        }
        return mapper.toResponse(repository.saveAndFlush(product));
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> findAll(Boolean active, Pageable pageable) {
        var products = active == null
                ? repository.findAll(pageable)
                : repository.findByActive(active, pageable);
        return products.map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public ProductResponseDTO findById(UUID id) {
        return mapper.toResponse(getById(id));
    }

    @Transactional(readOnly = true)
    public ProductResponseDTO findByCode(Integer code) {
        var product = repository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", "código " + code));
        return mapper.toResponse(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> findByName(String name, Pageable pageable) {
        return repository.findByNameContainingIgnoreCase(name.trim(), pageable)
                .map(mapper::toResponse);
    }

    @Transactional
    public ProductResponseDTO update(UUID id, ProductRequestDTO request) {
        var product = repository.findByIdForUpdate(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", id));
        mapper.update(request, product);
        return mapper.toResponse(repository.save(product));
    }

    @Transactional
    public ProductResponseDTO deactivate(UUID id) {
        Product product = repository.findByIdForUpdate(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", id));
        product.setActive(false);
        return mapper.toResponse(repository.save(product));
    }

    private Product getById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto", id));
    }

}

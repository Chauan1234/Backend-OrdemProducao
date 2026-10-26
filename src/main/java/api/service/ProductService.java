package api.service;

import api.service.exception.ProdutoNaoEncontradoException;

import api.dto.product.ProductRequestDTO;
import api.dto.product.ProductResponseDTO;
import api.model.Product;
import api.mapper.ProductMapper;
import api.repository.ProductRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Aplica as regras de negócio para o gerenciamento de produtos.
 */

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
                .orElseThrow(() -> new ProdutoNaoEncontradoException("código " + code));
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
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));
        mapper.update(request, product);
        return mapper.toResponse(repository.save(product));
    }

    @Transactional
    public ProductResponseDTO deactivate(UUID id) {
        Product product = repository.findByIdForUpdate(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));
        product.setActive(false);
        return mapper.toResponse(repository.save(product));
    }

    private Product getById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ProdutoNaoEncontradoException(id));
    }

}

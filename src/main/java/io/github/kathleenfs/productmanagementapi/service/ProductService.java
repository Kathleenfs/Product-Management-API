package io.github.kathleenfs.productmanagementapi.service;

import io.github.kathleenfs.productmanagementapi.domain.entity.Category;
import io.github.kathleenfs.productmanagementapi.domain.entity.Product;
import io.github.kathleenfs.productmanagementapi.dto.request.ProductRequestDTO;
import io.github.kathleenfs.productmanagementapi.dto.response.ProductResponseDTO;
import io.github.kathleenfs.productmanagementapi.exception.ResourceNotFoundException;
import io.github.kathleenfs.productmanagementapi.mapper.ProductMapper;
import io.github.kathleenfs.productmanagementapi.repository.CategoryRepository;
import io.github.kathleenfs.productmanagementapi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public ProductResponseDTO create(ProductRequestDTO request) {

        Category category = categoryRepository
                .findById(request.categoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found")
                );

        Product product = productMapper.toEntity(request);

        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        return productMapper.toResponse(savedProduct);
    }

    public ProductResponseDTO findById(Long id) {

        Product product = productRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found")
                );

        return productMapper.toResponse(product);
    }

    public List<ProductResponseDTO> findAll() {

        return productRepository.findAllByActiveTrue()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }
    public ProductResponseDTO update(Long id, ProductRequestDTO request) {

        Product product = productRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found")
                );

        Category category = categoryRepository
                .findById(request.categoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found")
                );

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStockQuantity(request.stockQuantity());
        product.setCategory(category);

        Product updatedProduct = productRepository.save(product);

        return productMapper.toResponse(updatedProduct);
    }

    public void deactivate(Long id) {

        Product product = productRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found")
                );

        product.setActive(false);

        productRepository.save(product);
    }

    public ProductResponseDTO activate(Long id) {

        Product product = productRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found")
                );

        product.setActive(true);

        Product activatedProduct = productRepository.save(product);

        return productMapper.toResponse(activatedProduct);
    }
}
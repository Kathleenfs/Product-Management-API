package io.github.kathleenfs.productmanagementapi.mapper;

import io.github.kathleenfs.productmanagementapi.domain.entity.Product;
import io.github.kathleenfs.productmanagementapi.dto.request.ProductRequestDTO;
import io.github.kathleenfs.productmanagementapi.dto.response.ProductResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequestDTO request) {

        Product product = new Product();

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStockQuantity(request.stockQuantity());

        return product;
    }

    public ProductResponseDTO toResponse(Product product) {

        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getActive(),
                product.getCategory().getId(),
                product.getCategory().getName(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
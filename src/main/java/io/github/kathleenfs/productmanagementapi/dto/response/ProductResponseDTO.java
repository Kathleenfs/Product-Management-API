package io.github.kathleenfs.productmanagementapi.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponseDTO(

        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity,
        Boolean active,
        Long categoryId,
        String categoryName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {
}
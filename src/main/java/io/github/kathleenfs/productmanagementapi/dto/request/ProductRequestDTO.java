package io.github.kathleenfs.productmanagementapi.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequestDTO(

        @NotBlank(message = "Name is required")
        @Size(max = 150, message = "Name must have at most 150 characters")
        String name,

        @Size(max = 500, message = "Description must have at most 500 characters")
        String description,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.01", message = "Price must be greater than zero")
        BigDecimal price,

        @NotNull(message = "Stock quantity is required")
        @Min(value = 0, message = "Stock quantity cannot be negative")
        Integer stockQuantity,

        @NotNull(message = "Category is required")
        Long categoryId

) {
}
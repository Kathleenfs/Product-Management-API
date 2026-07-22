package io.github.kathleenfs.productmanagementapi.mapper;

import io.github.kathleenfs.productmanagementapi.domain.entity.Category;
import io.github.kathleenfs.productmanagementapi.dto.request.CategoryRequestDTO;
import io.github.kathleenfs.productmanagementapi.dto.response.CategoryResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public Category toEntity(CategoryRequestDTO dto) {

        Category category = new Category();

        category.setName(dto.name());
        category.setDescription(dto.description());

        return category;
    }

    public CategoryResponseDTO toResponse(Category category) {

        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getActive(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}

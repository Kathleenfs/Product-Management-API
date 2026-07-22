package io.github.kathleenfs.productmanagementapi.service;

import io.github.kathleenfs.productmanagementapi.domain.entity.Category;
import io.github.kathleenfs.productmanagementapi.dto.request.CategoryRequestDTO;
import io.github.kathleenfs.productmanagementapi.dto.response.CategoryResponseDTO;
import io.github.kathleenfs.productmanagementapi.mapper.CategoryMapper;
import io.github.kathleenfs.productmanagementapi.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;


    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper =  categoryMapper;
    }

    public CategoryResponseDTO create(CategoryRequestDTO dto){

        Category category = categoryMapper.toEntity(dto);

        Category saved = categoryRepository.save(category);

        return categoryMapper.toResponse(saved);
    }
}

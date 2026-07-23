package io.github.kathleenfs.productmanagementapi.service;

import io.github.kathleenfs.productmanagementapi.domain.entity.Category;
import io.github.kathleenfs.productmanagementapi.dto.request.CategoryRequestDTO;
import io.github.kathleenfs.productmanagementapi.dto.response.CategoryResponseDTO;
import io.github.kathleenfs.productmanagementapi.exception.CategoryAlreadyExistsException;
import io.github.kathleenfs.productmanagementapi.exception.ResourceNotFoundException;
import io.github.kathleenfs.productmanagementapi.mapper.CategoryMapper;
import io.github.kathleenfs.productmanagementapi.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;


    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper =  categoryMapper;
    }

    public CategoryResponseDTO create(CategoryRequestDTO dto) {

        if (categoryRepository.existsByName(dto.name())) {
            throw new CategoryAlreadyExistsException(
                    "Category already exists"
            );
        }

        Category category = categoryMapper.toEntity(dto);

        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.toResponse(savedCategory);
    }

    public List<CategoryResponseDTO> findAll() {

        return categoryRepository.findAllByActiveTrue()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    public CategoryResponseDTO findById(Long id) {

        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found")
                );

        return categoryMapper.toResponse(category);
    }

    public CategoryResponseDTO update(Long id, CategoryRequestDTO dto) {

        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found")
                );

        if (categoryRepository.existsByNameAndIdNot(dto.name(), id)) {
            throw new CategoryAlreadyExistsException(
                    "Category already exists"
            );
        }
        category.setName(dto.name());
        category.setDescription(dto.description());

        Category updatedCategory = categoryRepository.save(category);

        return categoryMapper.toResponse(updatedCategory);
    }

    public void deactivate(Long id) {

        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found")
                );

        category.setActive(false);

        categoryRepository.save(category);
    }

    public CategoryResponseDTO activate(Long id) {

        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found")
                );

        category.setActive(true);

        Category activatedCategory = categoryRepository.save(category);

        return categoryMapper.toResponse(activatedCategory);
    }
}


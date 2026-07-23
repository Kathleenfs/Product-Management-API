package io.github.kathleenfs.productmanagementapi.controller;

import io.github.kathleenfs.productmanagementapi.dto.request.CategoryRequestDTO;
import io.github.kathleenfs.productmanagementapi.dto.response.CategoryResponseDTO;
import io.github.kathleenfs.productmanagementapi.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDTO create(
            @Valid @RequestBody CategoryRequestDTO dto
    ) {
        return categoryService.create(dto);
    }

    @GetMapping("/{id}")
    public CategoryResponseDTO findById(@PathVariable Long id) {
        return categoryService.findById(id);
    }

    @GetMapping
    public List<CategoryResponseDTO> findAll() {
        return categoryService.findAll();
    }

    @PutMapping("/{id}")
    public CategoryResponseDTO update(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequestDTO dto
    ) {
        return categoryService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable Long id) {
        categoryService.deactivate(id);
    }

    @PatchMapping("/{id}/activate")
    public CategoryResponseDTO activate(@PathVariable Long id) {
        return categoryService.activate(id);
    }
}

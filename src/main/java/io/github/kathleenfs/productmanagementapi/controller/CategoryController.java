package io.github.kathleenfs.productmanagementapi.controller;

import io.github.kathleenfs.productmanagementapi.domain.entity.Category;
import io.github.kathleenfs.productmanagementapi.dto.request.CategoryRequestDTO;
import io.github.kathleenfs.productmanagementapi.dto.response.CategoryResponseDTO;
import io.github.kathleenfs.productmanagementapi.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
            @RequestBody CategoryRequestDTO dto){
        return categoryService.create(dto);
    }

}

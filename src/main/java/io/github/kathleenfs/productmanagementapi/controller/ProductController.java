package io.github.kathleenfs.productmanagementapi.controller;

import io.github.kathleenfs.productmanagementapi.dto.request.ProductRequestDTO;
import io.github.kathleenfs.productmanagementapi.dto.response.ProductResponseDTO;
import io.github.kathleenfs.productmanagementapi.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDTO create(
            @Valid @RequestBody ProductRequestDTO request
    ) {
        return productService.create(request);
    }

    @GetMapping
    public List<ProductResponseDTO> findAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ProductResponseDTO findById(
            @PathVariable Long id
    ) {
        return productService.findById(id);
    }

    @PutMapping("/{id}")
    public ProductResponseDTO update(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDTO request
    ) {
        return productService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(
            @PathVariable Long id
    ) {
        productService.deactivate(id);
    }

    @PatchMapping("/{id}/activate")
    public ProductResponseDTO activate(
            @PathVariable Long id
    ) {
        return productService.activate(id);
    }
}
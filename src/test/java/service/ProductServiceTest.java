package service;

import io.github.kathleenfs.productmanagementapi.domain.entity.Category;
import io.github.kathleenfs.productmanagementapi.domain.entity.Product;
import io.github.kathleenfs.productmanagementapi.dto.request.ProductRequestDTO;
import io.github.kathleenfs.productmanagementapi.dto.response.ProductResponseDTO;
import io.github.kathleenfs.productmanagementapi.exception.ResourceNotFoundException;
import io.github.kathleenfs.productmanagementapi.mapper.ProductMapper;
import io.github.kathleenfs.productmanagementapi.repository.CategoryRepository;
import io.github.kathleenfs.productmanagementapi.repository.ProductRepository;
import io.github.kathleenfs.productmanagementapi.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper productMapper;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(
                productRepository,
                categoryRepository,
                productMapper
        );
    }

    @Test
    void shouldCreateProductSuccessfully() {

        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        ProductRequestDTO request = new ProductRequestDTO(
                "Notebook",
                "Gaming notebook",
                new BigDecimal("5999.90"),
                10,
                1L
        );

        Product product = new Product();
        product.setName("Notebook");
        product.setCategory(category);

        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setName("Notebook");
        savedProduct.setCategory(category);

        ProductResponseDTO response = new ProductResponseDTO(
                1L,
                "Notebook",
                "Gaming notebook",
                new BigDecimal("5999.90"),
                10,
                true,
                1L,
                "Electronics",
                null,
                null
        );

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(productMapper.toEntity(request))
                .thenReturn(product);

        when(productRepository.save(product))
                .thenReturn(savedProduct);

        when(productMapper.toResponse(savedProduct))
                .thenReturn(response);

        ProductResponseDTO result = productService.create(request);

        assertEquals(1L, result.id());
        assertEquals("Notebook", result.name());

        verify(categoryRepository).findById(1L);
        verify(productRepository).save(product);
    }

    @Test
    void shouldThrowExceptionWhenCategoryDoesNotExist() {

        ProductRequestDTO request = new ProductRequestDTO(
                "Notebook",
                "Gaming notebook",
                new BigDecimal("5999.90"),
                10,
                999L
        );

        when(categoryRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> productService.create(request)
        );

        verify(categoryRepository).findById(999L);

        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldFindProductByIdSuccessfully() {

        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        Product product = new Product();
        product.setId(1L);
        product.setName("Notebook");
        product.setCategory(category);

        ProductResponseDTO response = new ProductResponseDTO(
                1L,
                "Notebook",
                "Gaming notebook",
                new BigDecimal("5999.90"),
                10,
                true,
                1L,
                "Electronics",
                null,
                null
        );

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        when(productMapper.toResponse(product))
                .thenReturn(response);

        ProductResponseDTO result = productService.findById(1L);

        assertEquals(1L, result.id());
        assertEquals("Notebook", result.name());

        verify(productRepository).findById(1L);
        verify(productMapper).toResponse(product);
    }

    @Test
    void shouldThrowExceptionWhenProductDoesNotExist() {

        when(productRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> productService.findById(999L)
        );

        verify(productRepository).findById(999L);

        verify(productMapper, never())
                .toResponse(any());
    }

    @Test
    void shouldFindAllActiveProducts() {

        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Notebook");
        product1.setCategory(category);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Mouse");
        product2.setCategory(category);

        ProductResponseDTO response1 = new ProductResponseDTO(
                1L,
                "Notebook",
                "Gaming notebook",
                new BigDecimal("5999.90"),
                10,
                true,
                1L,
                "Electronics",
                null,
                null
        );

        ProductResponseDTO response2 = new ProductResponseDTO(
                2L,
                "Mouse",
                "Wireless mouse",
                new BigDecimal("199.90"),
                20,
                true,
                1L,
                "Electronics",
                null,
                null
        );

        when(productRepository.findAllByActiveTrue())
                .thenReturn(List.of(product1, product2));

        when(productMapper.toResponse(product1))
                .thenReturn(response1);

        when(productMapper.toResponse(product2))
                .thenReturn(response2);

        List<ProductResponseDTO> result = productService.findAll();

        assertEquals(2, result.size());
        assertEquals("Notebook", result.get(0).name());
        assertEquals("Mouse", result.get(1).name());

        verify(productRepository).findAllByActiveTrue();
    }
    @Test
    void shouldUpdateProductSuccessfully() {

        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        Product product = new Product();
        product.setId(1L);
        product.setName("Notebook");

        ProductRequestDTO request = new ProductRequestDTO(
                "Notebook Pro",
                "Updated notebook",
                new BigDecimal("6499.90"),
                8,
                1L
        );

        Product updatedProduct = new Product();
        updatedProduct.setId(1L);
        updatedProduct.setName("Notebook Pro");
        updatedProduct.setDescription("Updated notebook");
        updatedProduct.setPrice(new BigDecimal("6499.90"));
        updatedProduct.setStockQuantity(8);
        updatedProduct.setCategory(category);

        ProductResponseDTO response = new ProductResponseDTO(
                1L,
                "Notebook Pro",
                "Updated notebook",
                new BigDecimal("6499.90"),
                8,
                true,
                1L,
                "Electronics",
                null,
                null
        );

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(productRepository.save(product))
                .thenReturn(updatedProduct);

        when(productMapper.toResponse(updatedProduct))
                .thenReturn(response);

        ProductResponseDTO result =
                productService.update(1L, request);

        assertEquals("Notebook Pro", result.name());
        assertEquals(new BigDecimal("6499.90"), result.price());
        assertEquals(8, result.stockQuantity());

        verify(productRepository).findById(1L);
        verify(categoryRepository).findById(1L);
        verify(productRepository).save(product);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingProduct() {

        ProductRequestDTO request = new ProductRequestDTO(
                "Notebook Pro",
                "Updated notebook",
                new BigDecimal("6499.90"),
                8,
                1L
        );

        when(productRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> productService.update(999L, request)
        );

        verify(productRepository).findById(999L);
        verify(categoryRepository, never()).findById(any());
        verify(productRepository, never()).save(any());
    }
    @Test
    void shouldThrowExceptionWhenUpdatingWithNonExistingCategory() {

        Product product = new Product();
        product.setId(1L);
        product.setName("Notebook");

        ProductRequestDTO request = new ProductRequestDTO(
                "Notebook Pro",
                "Updated notebook",
                new BigDecimal("6499.90"),
                8,
                999L
        );

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        when(categoryRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> productService.update(1L, request)
        );

        verify(productRepository).findById(1L);
        verify(categoryRepository).findById(999L);
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldDeactivateProductSuccessfully() {

        Product product = new Product();
        product.setId(1L);
        product.setName("Notebook");
        product.setActive(true);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        productService.deactivate(1L);

        assertFalse(product.getActive());

        verify(productRepository).findById(1L);
        verify(productRepository).save(product);
    }

    @Test
    void shouldThrowExceptionWhenDeactivatingNonExistingProduct() {

        when(productRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> productService.deactivate(999L)
        );

        verify(productRepository).findById(999L);
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldActivateProductSuccessfully() {

        Product product = new Product();
        product.setId(1L);
        product.setName("Notebook");
        product.setActive(false);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        when(productRepository.save(product))
                .thenReturn(product);

        when(productMapper.toResponse(product))
                .thenReturn(new ProductResponseDTO(
                        1L,
                        "Notebook",
                        null,
                        new BigDecimal("5999.90"),
                        10,
                        true,
                        1L,
                        "Electronics",
                        null,
                        null
                ));

        ProductResponseDTO result = productService.activate(1L);

        assertTrue(product.getActive());
        assertTrue(result.active());

        verify(productRepository).findById(1L);
        verify(productRepository).save(product);
    }

    @Test
    void shouldThrowExceptionWhenActivatingNonExistingProduct() {

        when(productRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> productService.activate(999L)
        );

        verify(productRepository).findById(999L);
        verify(productRepository, never()).save(any());
    }
}
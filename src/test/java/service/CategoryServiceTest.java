package service;

import io.github.kathleenfs.productmanagementapi.domain.entity.Category;
import io.github.kathleenfs.productmanagementapi.dto.request.CategoryRequestDTO;
import io.github.kathleenfs.productmanagementapi.dto.response.CategoryResponseDTO;
import io.github.kathleenfs.productmanagementapi.exception.CategoryAlreadyExistsException;
import io.github.kathleenfs.productmanagementapi.exception.ResourceNotFoundException;
import io.github.kathleenfs.productmanagementapi.mapper.CategoryMapper;
import io.github.kathleenfs.productmanagementapi.repository.CategoryRepository;
import io.github.kathleenfs.productmanagementapi.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryService(
                categoryRepository,
                categoryMapper
        );
    }

    @Test
    void shouldCreateCategorySuccessfully() {

        CategoryRequestDTO request = new CategoryRequestDTO(
                "Electronics",
                "Electronic products"
        );

        Category category = new Category();
        category.setName("Electronics");

        Category savedCategory = new Category();
        savedCategory.setId(1L);
        savedCategory.setName("Electronics");

        CategoryResponseDTO response = new CategoryResponseDTO(
                1L,
                "Electronics",
                "Electronic products",
                true,
                null,
                null
        );

        when(categoryRepository.existsByName("Electronics"))
                .thenReturn(false);

        when(categoryMapper.toEntity(request))
                .thenReturn(category);

        when(categoryRepository.save(category))
                .thenReturn(savedCategory);

        when(categoryMapper.toResponse(savedCategory))
                .thenReturn(response);

        CategoryResponseDTO result =
                categoryService.create(request);

        assertEquals(1L, result.id());
        assertEquals("Electronics", result.name());

        verify(categoryRepository)
                .existsByName("Electronics");

        verify(categoryRepository)
                .save(category);
    }

    @Test
    void shouldThrowExceptionWhenCategoryNameAlreadyExists() {

        CategoryRequestDTO request = new CategoryRequestDTO(
                "Electronics",
                "Electronic products"
        );

        when(categoryRepository.existsByName("Electronics"))
                .thenReturn(true);

        assertThrows(
                CategoryAlreadyExistsException.class,
                () -> categoryService.create(request)
        );

        verify(categoryRepository)
                .existsByName("Electronics");

        verify(categoryRepository, never())
                .save(any());
    }

    @Test
    void shouldFindCategoryByIdSuccessfully() {

        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        CategoryResponseDTO response = new CategoryResponseDTO(
                1L,
                "Electronics",
                "Electronic products",
                true,
                null,
                null
        );

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(categoryMapper.toResponse(category))
                .thenReturn(response);

        CategoryResponseDTO result =
                categoryService.findById(1L);

        assertEquals(1L, result.id());
        assertEquals("Electronics", result.name());

        verify(categoryRepository).findById(1L);
        verify(categoryMapper).toResponse(category);
    }

    @Test
    void shouldThrowExceptionWhenCategoryDoesNotExist() {

        when(categoryRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> categoryService.findById(999L)
        );

        verify(categoryRepository).findById(999L);

        verify(categoryMapper, never())
                .toResponse(any());
    }
    @Test
    void shouldFindAllActiveCategories() {

        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Electronics");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Books");

        CategoryResponseDTO response1 = new CategoryResponseDTO(
                1L,
                "Electronics",
                null,
                true,
                null,
                null
        );

        CategoryResponseDTO response2 = new CategoryResponseDTO(
                2L,
                "Books",
                null,
                true,
                null,
                null
        );

        when(categoryRepository.findAllByActiveTrue())
                .thenReturn(List.of(category1, category2));

        when(categoryMapper.toResponse(category1))
                .thenReturn(response1);

        when(categoryMapper.toResponse(category2))
                .thenReturn(response2);

        List<CategoryResponseDTO> result =
                categoryService.findAll();

        assertEquals(2, result.size());
        assertEquals("Electronics", result.get(0).name());
        assertEquals("Books", result.get(1).name());

        verify(categoryRepository).findAllByActiveTrue();
    }
    @Test
    void shouldUpdateCategorySuccessfully() {

        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        CategoryRequestDTO request = new CategoryRequestDTO(
                "Technology",
                "Technology products"
        );

        CategoryResponseDTO response = new CategoryResponseDTO(
                1L,
                "Technology",
                "Technology products",
                true,
                null,
                null
        );

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(categoryRepository.existsByNameAndIdNot("Technology", 1L))
                .thenReturn(false);

        when(categoryRepository.save(category))
                .thenReturn(category);

        when(categoryMapper.toResponse(category))
                .thenReturn(response);

        CategoryResponseDTO result =
                categoryService.update(1L, request);

        assertEquals("Technology", result.name());

        verify(categoryRepository).findById(1L);
        verify(categoryRepository)
                .existsByNameAndIdNot("Technology", 1L);

        verify(categoryRepository).save(category);
    }

    @Test
    void shouldDeactivateCategorySuccessfully() {

        Category category = new Category();
        category.setId(1L);
        category.setActive(true);

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        categoryService.deactivate(1L);

        assertFalse(category.getActive());

        verify(categoryRepository).save(category);
    }

    @Test
    void shouldActivateCategorySuccessfully() {

        Category category = new Category();
        category.setId(1L);
        category.setActive(false);

        CategoryResponseDTO response = new CategoryResponseDTO(
                1L,
                "Electronics",
                null,
                true,
                null,
                null
        );

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(categoryRepository.save(category))
                .thenReturn(category);

        when(categoryMapper.toResponse(category))
                .thenReturn(response);

        CategoryResponseDTO result =
                categoryService.activate(1L);

        assertTrue(category.getActive());
        assertTrue(result.active());

        verify(categoryRepository).findById(1L);
        verify(categoryRepository).save(category);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithExistingCategoryName() {

        Category category = new Category();
        category.setId(1L);

        CategoryRequestDTO request = new CategoryRequestDTO(
                "Technology",
                "Technology products"
        );

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(categoryRepository.existsByNameAndIdNot("Technology", 1L))
                .thenReturn(true);

        assertThrows(
                CategoryAlreadyExistsException.class,
                () -> categoryService.update(1L, request)
        );

        verify(categoryRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenDeactivatingNonExistingCategory() {

        when(categoryRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> categoryService.deactivate(999L)
        );

        verify(categoryRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenActivatingNonExistingCategory() {

        when(categoryRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> categoryService.activate(999L)
        );

        verify(categoryRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingCategory() {

        CategoryRequestDTO request = new CategoryRequestDTO(
                "Technology",
                "Technology products"
        );

        when(categoryRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> categoryService.update(999L, request)
        );

        verify(categoryRepository).findById(999L);

        verify(categoryRepository, never())
                .existsByNameAndIdNot(anyString(), anyLong());

        verify(categoryRepository, never())
                .save(any());
    }
}

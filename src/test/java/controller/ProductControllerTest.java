package io.github.kathleenfs.productmanagementapi.controller;

import io.github.kathleenfs.productmanagementapi.security.CustomUserDetailsService;
import io.github.kathleenfs.productmanagementapi.service.JwtService;
import io.github.kathleenfs.productmanagementapi.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void shouldCreateProductSuccessfully() throws Exception {

        mockMvc.perform(
                        post("/products")
                                .contentType("application/json")
                                .content("""
                                        {
                                          "name": "Notebook",
                                          "description": "Gaming notebook",
                                          "price": 5999.90,
                                          "stockQuantity": 10,
                                          "categoryId": 1
                                        }
                                        """)
                )
                .andExpect(status().isCreated());
    }
    @Test
    void shouldReturnBadRequestWhenProductIsInvalid() throws Exception {

        mockMvc.perform(
                        post("/products")
                                .contentType("application/json")
                                .content("""
                                    {
                                      "name": "",
                                      "description": "Invalid product",
                                      "price": 0,
                                      "stockQuantity": -1,
                                      "categoryId": null
                                    }
                                    """)
                )
                .andExpect(status().isBadRequest());
    }
}
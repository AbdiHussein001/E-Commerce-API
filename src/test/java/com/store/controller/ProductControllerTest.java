package com.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.dto.Label;
import com.store.entity.Product;
import com.store.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private Product product;

    @BeforeEach
    public void setUp() {
        product = new Product();
        product.setProductId(1L);
        product.setName("Fanta");
        product.setPrice(5.99);
        product.setLabels(Set.of(Label.DRINK));
    }

    @Test
    public void testGetAllProducts() throws Exception {
        when(productService.getAllProducts()).thenReturn(Collections.singletonList(product));

        MvcResult mvcResult = mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        List<Product> products = objectMapper.readValue(jsonResponse,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Product.class));

        assertThat(products).hasSize(1);
        assertThat(products.get(0).getProductId()).isEqualTo(1L);
        assertThat(products.get(0).getName()).isEqualTo("Fanta");
        assertThat(products.get(0).getPrice()).isEqualTo(5.99);
        assertThat(products.get(0).getLabels()).containsExactly(Label.DRINK);

    }

    @Test
    public void testGetProductById() throws Exception {
        when(productService.getProductById(1L)).thenReturn(product);

        MvcResult mvcResult = mockMvc.perform(get("/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        Product returnedProduct = objectMapper.readValue(jsonResponse, Product.class);

        assertThat(returnedProduct.getProductId()).isEqualTo(1L);
        assertThat(returnedProduct.getName()).isEqualTo("Fanta");
        assertThat(returnedProduct.getPrice()).isEqualTo(5.99);
        assertThat(returnedProduct.getLabels()).containsExactly(Label.DRINK);
    }

    @Test
    public void testCreateProduct() throws Exception {
        when(productService.createProduct(any(Product.class))).thenReturn(product);

        MvcResult mvcResult = mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        Product createdProduct = objectMapper.readValue(jsonResponse, Product.class);

        assertThat(createdProduct.getProductId()).isEqualTo(1L);
        assertThat(createdProduct.getName()).isEqualTo("Fanta");
        assertThat(createdProduct.getPrice()).isEqualTo(5.99);
        assertThat(createdProduct.getLabels()).containsExactly(Label.DRINK);
    }

    @Test
    public void testDeleteProduct() throws Exception {
        Mockito.doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(productService, Mockito.times(1)).deleteProduct(1L);
    }
}
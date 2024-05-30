package com.store.service;

import com.store.entity.Product;
import com.store.exception.BadRequestException;
import com.store.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.store.dto.Label.DRINK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        product = new Product();
        product.setProductId(1L);
        product.setName("Fanta");
        product.setPrice(5.99);
        product.setLabels(Set.of(DRINK));
    }

    @Test
    public void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(product));

        List<Product> products = productService.getAllProducts();

        assertThat(products).hasSize(1);
        assertThat(products.get(0).getProductId()).isEqualTo(1L);
        assertThat(products.get(0).getName()).isEqualTo("Fanta");
    }

    @Test
    public void testGetProductById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product foundProduct = productService.getProductById(1L);

        assertThat(foundProduct.getProductId()).isEqualTo(1L);
        assertThat(foundProduct.getName()).isEqualTo("Fanta");
    }

    @Test
    public void testGetProductById_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        BadRequestException exception = assertThrows(BadRequestException.class, () -> productService.getProductById(1L));

        assertThat(exception.getMessage()).isEqualTo("Product with id: 1 not found");
    }

    @Test
    public void testCreateProduct() {
        when(productRepository.existsByName(product.getName())).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product createdProduct = productService.createProduct(product);

        assertThat(createdProduct.getProductId()).isEqualTo(1L);
        assertThat(createdProduct.getName()).isEqualTo("Fanta");
        assertThat(createdProduct.getAddedAt()).isEqualTo(LocalDate.now());
    }

    @Test
    public void testCreateProduct_DuplicateName() {
        when(productRepository.existsByName(product.getName())).thenReturn(true);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> productService.createProduct(product));

        assertThat(exception.getMessage()).isEqualTo("Not able to create product, product with the same name already exists.");
    }

    @Test
    public void testDeleteProduct() {
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }

}

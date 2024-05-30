package com.store.service;

import com.store.entity.Product;
import com.store.exception.BadRequestException;
import com.store.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new BadRequestException("Product with id: " + id + " not found"));
    }

    public Product createProduct(Product product) {
        if (productRepository.existsByName(product.getName())) {
            throw new BadRequestException("Not able to create product, product with the same name already exists.");
        }
        product.setAddedAt(LocalDate.now());
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}

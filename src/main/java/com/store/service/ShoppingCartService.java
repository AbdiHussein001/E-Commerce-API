package com.store.service;

import com.store.dto.CartItemDTO;
import com.store.dto.ShoppingCartDTO;
import com.store.entity.CartItem;
import com.store.entity.Product;
import com.store.entity.ShoppingCart;
import com.store.exception.BadRequestException;
import com.store.repository.ProductRepository;
import com.store.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShoppingCartService {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private ProductRepository productRepository;

    public ShoppingCart createCart() {
        ShoppingCart cart = new ShoppingCart();
        return shoppingCartRepository.save(cart);
    }

    public List<ShoppingCartDTO> getAllCarts() {
        return shoppingCartRepository.findAll().stream().map(ShoppingCartDTO::new).collect(Collectors.toList());
    }

    public ShoppingCart getCartById(Long id) {
        return shoppingCartRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Cart with id: " + id + " not found"));
    }

    public ShoppingCart updateCart(Long cartId, List<CartItemDTO> items) {
        ShoppingCart cart = getCartById(cartId);

        if (cart.isCheckedOut()) {
            throw new BadRequestException("Cannot modify a checked-out cart");
        }

        cart.getProducts().clear();
        for (CartItemDTO item : items) {

            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new BadRequestException("Product with id: " + item.getProductId() + " not found"));
            CartItem cartItem = new CartItem();
            cartItem.setQuantity(item.getQuantity());
            cartItem.setProduct(product);
            cart.getProducts().add(cartItem);
        }

        return shoppingCartRepository.save(cart);
    }

    public ShoppingCart checkoutCart(Long cartId) {
        ShoppingCart cart = getCartById(cartId);

        cart.setCheckedOut(true);
        return shoppingCartRepository.save(cart);
    }

    public double calculateTotalCost(ShoppingCart cart) {
        return cart.getProducts().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }

}

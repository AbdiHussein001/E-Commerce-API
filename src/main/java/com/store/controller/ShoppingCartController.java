package com.store.controller;

import com.store.dto.CartItemDTO;
import com.store.dto.CheckoutResponse;
import com.store.dto.ShoppingCartDTO;
import com.store.entity.ShoppingCart;
import com.store.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping
    public ShoppingCart createCart() {
        return shoppingCartService.createCart();
    }

    @GetMapping
    public List<ShoppingCartDTO> getAllCarts() {
        return shoppingCartService.getAllCarts();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShoppingCartDTO> updateCart(@PathVariable Long id, @RequestBody List<CartItemDTO> items) {
        ShoppingCart updatedCart = shoppingCartService.updateCart(id, items);
        return ResponseEntity.ok(new ShoppingCartDTO(updatedCart));
    }

    @PostMapping("/{id}/checkout")
    public ResponseEntity<?> checkoutCart(@PathVariable Long id) {
        ShoppingCart cart = shoppingCartService.checkoutCart(id);
        double totalCost = shoppingCartService.calculateTotalCost(cart);
        return ResponseEntity.ok(new CheckoutResponse(cart, totalCost));
    }
}

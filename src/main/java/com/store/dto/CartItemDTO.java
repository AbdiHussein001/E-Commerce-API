package com.store.dto;

import com.store.entity.CartItem;
import lombok.Data;

@Data
public class CartItemDTO {

    private Long productId;
    private int quantity;

    public CartItemDTO() {

    }

    public CartItemDTO(CartItem cartItem) {
        this.productId = cartItem.getProduct().getProductId();
        this.quantity = cartItem.getQuantity();
    }
}

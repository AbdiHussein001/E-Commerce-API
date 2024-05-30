package com.store.dto;

import com.store.entity.ShoppingCart;
import lombok.Data;

@Data
public class CheckoutResponse {

    private ShoppingCartDTO cart;
    private double totalCost;

    public CheckoutResponse(ShoppingCart cart, double totalCost) {
        this.cart = new ShoppingCartDTO(cart);
        this.totalCost = totalCost;
    }
}

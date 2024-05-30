package com.store.dto;


import com.store.entity.ShoppingCart;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ShoppingCartDTO {

    private Long cartId;
    private List<CartItemDTO> products;
    private boolean checkedOut;

    public ShoppingCartDTO() {
    }

    public ShoppingCartDTO(ShoppingCart shoppingCart) {
        this.cartId = shoppingCart.getCartId();
        this.products = shoppingCart.getProducts().stream().map(CartItemDTO::new).collect(Collectors.toList());
        this.checkedOut = shoppingCart.isCheckedOut();
    }

}

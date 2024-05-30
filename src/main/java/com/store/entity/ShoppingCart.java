package com.store.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @OneToMany(cascade = CascadeType.ALL)
    private List<CartItem> products = new ArrayList<>();

    private boolean checkedOut = false;
}

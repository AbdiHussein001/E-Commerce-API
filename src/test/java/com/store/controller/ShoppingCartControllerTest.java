package com.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.dto.CartItemDTO;
import com.store.dto.CheckoutResponse;
import com.store.dto.ShoppingCartDTO;
import com.store.entity.ShoppingCart;
import com.store.service.ShoppingCartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShoppingCartController.class)
public class ShoppingCartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShoppingCartService shoppingCartService;

    @Autowired
    private ObjectMapper objectMapper;

    private ShoppingCart shoppingCart;

    @BeforeEach
    public void setUp() {
        shoppingCart = new ShoppingCart();
        shoppingCart.setCartId(1L);
        shoppingCart.setCheckedOut(false);
    }

    @Test
    public void testCreateCart() throws Exception {
        when(shoppingCartService.createCart()).thenReturn(shoppingCart);

        MvcResult mvcResult = mockMvc.perform(post("/carts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ShoppingCart createdCart = objectMapper.readValue(jsonResponse, ShoppingCart.class);

        assertThat(createdCart.getCartId()).isEqualTo(1L);
        assertThat(createdCart.isCheckedOut()).isFalse();
    }

    @Test
    public void testGetAllCarts() throws Exception {
        when(shoppingCartService.getAllCarts()).thenReturn(Collections.singletonList(new ShoppingCartDTO(shoppingCart)));

        MvcResult mvcResult = mockMvc.perform(get("/carts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        List<ShoppingCartDTO> carts = objectMapper.readValue(jsonResponse, objectMapper.getTypeFactory().constructCollectionType(List.class, ShoppingCartDTO.class));

        assertThat(carts).hasSize(1);
        assertThat(carts.get(0).getCartId()).isEqualTo(1L);
        assertThat(carts.get(0).isCheckedOut()).isFalse();
    }

    @Test
    public void testUpdateCart() throws Exception {
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setProductId(123L);
        cartItemDTO.setQuantity(2);
        List<CartItemDTO> items = List.of(cartItemDTO);
        ShoppingCart updatedCart = new ShoppingCart();
        updatedCart.setCartId(1L);
        updatedCart.setCheckedOut(false);

        when(shoppingCartService.updateCart(anyLong(), any())).thenReturn(updatedCart);

        MvcResult mvcResult = mockMvc.perform(put("/carts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(items)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ShoppingCartDTO responseCart = objectMapper.readValue(jsonResponse, ShoppingCartDTO.class);

        assertThat(responseCart.getCartId()).isEqualTo(1L);
        assertThat(responseCart.isCheckedOut()).isFalse();
    }

    @Test
    public void testCheckoutCart() throws Exception {
        ShoppingCart checkedOutCart = new ShoppingCart();
        checkedOutCart.setCartId(1L);
        checkedOutCart.setCheckedOut(true);
        double totalCost = 11.98;

        when(shoppingCartService.checkoutCart(anyLong())).thenReturn(checkedOutCart);
        when(shoppingCartService.calculateTotalCost(any())).thenReturn(totalCost);

        MvcResult mvcResult = mockMvc.perform(post("/carts/1/checkout")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        CheckoutResponse response = objectMapper.readValue(jsonResponse, CheckoutResponse.class);

        assertThat(response.getCart().getCartId()).isEqualTo(1L);
        assertThat(response.getCart().isCheckedOut()).isTrue();
        assertThat(response.getTotalCost()).isEqualTo(11.98);
    }
}

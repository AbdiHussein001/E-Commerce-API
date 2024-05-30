package com.store.service;

import com.store.dto.CartItemDTO;
import com.store.dto.ShoppingCartDTO;
import com.store.entity.CartItem;
import com.store.entity.Product;
import com.store.entity.ShoppingCart;
import com.store.exception.BadRequestException;
import com.store.repository.ProductRepository;
import com.store.repository.ShoppingCartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ShoppingCartServiceTest {

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ShoppingCartService shoppingCartService;

    private ShoppingCart cart;
    private Product product;
    private CartItemDTO cartItemDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        cart = new ShoppingCart();
        cart.setCartId(1L);
        cart.setCheckedOut(false);

        product = new Product();
        product.setProductId(1L);
        product.setName("Fanta");
        product.setPrice(5.99);

        cartItemDTO = new CartItemDTO();
        cartItemDTO.setProductId(1L);
        cartItemDTO.setQuantity(2);
    }

    @Test
    public void testCreateCart() {
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(cart);

        ShoppingCart createdCart = shoppingCartService.createCart();

        assertThat(createdCart.getCartId()).isEqualTo(1L);
        verify(shoppingCartRepository, times(1)).save(any(ShoppingCart.class));
    }

    @Test
    public void testGetAllCarts() {
        when(shoppingCartRepository.findAll()).thenReturn(Arrays.asList(cart));

        List<ShoppingCartDTO> carts = shoppingCartService.getAllCarts();

        assertThat(carts).hasSize(1);
        assertThat(carts.get(0).getCartId()).isEqualTo(1L);
        verify(shoppingCartRepository, times(1)).findAll();
    }

    @Test
    public void testGetCartById() {
        when(shoppingCartRepository.findById(1L)).thenReturn(Optional.of(cart));

        ShoppingCart foundCart = shoppingCartService.getCartById(1L);

        assertThat(foundCart.getCartId()).isEqualTo(1L);
    }

    @Test
    public void testGetCartById_NotFound() {
        when(shoppingCartRepository.findById(1L)).thenReturn(Optional.empty());

        BadRequestException exception = assertThrows(BadRequestException.class, () -> shoppingCartService.getCartById(1L));

        assertThat(exception.getMessage()).isEqualTo("Cart with id: 1 not found");
    }

    @Test
    public void testUpdateCart() {
        when(shoppingCartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(cart);

        ShoppingCart updatedCart = shoppingCartService.updateCart(1L, Arrays.asList(cartItemDTO));

        assertThat(updatedCart.getProducts()).hasSize(1);
        assertThat(updatedCart.getProducts().get(0).getProduct().getProductId()).isEqualTo(1L);
        assertThat(updatedCart.getProducts().get(0).getQuantity()).isEqualTo(2);
    }

    @Test
    public void testUpdateCart_CartNotFound() {
        when(shoppingCartRepository.findById(1L)).thenReturn(Optional.empty());

        BadRequestException exception = assertThrows(BadRequestException.class, () -> shoppingCartService.updateCart(1L, Arrays.asList(cartItemDTO)));

        assertThat(exception.getMessage()).isEqualTo("Cart with id: 1 not found");
    }

    @Test
    public void testUpdateCart_ProductNotFound() {
        when(shoppingCartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        BadRequestException exception = assertThrows(BadRequestException.class, () -> shoppingCartService.updateCart(1L, Arrays.asList(cartItemDTO)));

        assertThat(exception.getMessage()).isEqualTo("Product with id: 1 not found");
    }

    @Test
    public void testCheckoutCart() {
        when(shoppingCartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(cart);

        ShoppingCart checkedOutCart = shoppingCartService.checkoutCart(1L);

        assertThat(checkedOutCart.isCheckedOut()).isTrue();
    }

    @Test
    public void testCalculateTotalCost() {
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        cart.setProducts(Arrays.asList(cartItem));

        double totalCost = shoppingCartService.calculateTotalCost(cart);

        assertThat(totalCost).isEqualTo(11.98);
    }
}

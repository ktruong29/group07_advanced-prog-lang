package com.example.group7project.controller;

import com.example.group7project.dto.AppResponseDTO;
import com.example.group7project.dto.CartRequestDTO;
import com.example.group7project.dto.CartResponseDTO;
import com.example.group7project.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public AppResponseDTO<CartResponseDTO> getCustomerCart() {
        String usernameFromAuth = SecurityContextHolder.getContext().getAuthentication().getName();
        CartResponseDTO cartResponseDTO = cartService.getCartByUsername(usernameFromAuth);
        return new AppResponseDTO<>(HttpStatus.OK, cartResponseDTO);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public AppResponseDTO<String> addProductToCart(@RequestBody CartRequestDTO cartRequestDTO) {
        String usernameFromAuth = SecurityContextHolder.getContext().getAuthentication().getName();
        cartService.addProductToCart(usernameFromAuth, cartRequestDTO);
        return new AppResponseDTO<>(HttpStatus.CREATED, "Product added to cart successfully");
    }

    @PutMapping("/product")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public AppResponseDTO<String> modifyProductInCart(@RequestBody CartRequestDTO cartRequestDTO) {
        String usernameFromAuth = SecurityContextHolder.getContext().getAuthentication().getName();
        cartService.updateProductInCart(usernameFromAuth, cartRequestDTO);
        return new AppResponseDTO<>(HttpStatus.OK, "Product added to cart successfully");
    }

    @DeleteMapping("/product/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public AppResponseDTO<String> removeProductFromCart(@PathVariable Long id) {
        String usernameFromAuth = SecurityContextHolder.getContext().getAuthentication().getName();
        cartService.removeProductFromCart(usernameFromAuth, id);
        return new AppResponseDTO<>(HttpStatus.NO_CONTENT, "Product removed from cart successfully");
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public AppResponseDTO<String> removeCart() {
        String usernameFromAuth = SecurityContextHolder.getContext().getAuthentication().getName();
        cartService.clearCart(usernameFromAuth);
        return new AppResponseDTO<>(HttpStatus.NO_CONTENT, "Cart removed successfully");
    }
}

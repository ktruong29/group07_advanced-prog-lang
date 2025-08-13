package com.example.group7project.service;

import com.example.group7project.dto.CartRequestDTO;
import com.example.group7project.dto.CartResponseDTO;
import com.example.group7project.entity.Cart;
import com.example.group7project.entity.CartDetail;
import com.example.group7project.entity.Customer;
import com.example.group7project.entity.Product;
import com.example.group7project.exception.CartServiceBusinessException;
import com.example.group7project.repository.CartDetailRepository;
import com.example.group7project.repository.CartRepository;
import com.example.group7project.repository.CustomerRepository;
import com.example.group7project.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@Transactional
public class CartService {
    private final CartRepository cartRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final CartDetailRepository cartDetailRepository;

    @Autowired
    public CartService(CartRepository cartRepository,
                       CustomerRepository customerRepository,
                       ProductRepository productRepository,
                       CartDetailRepository cartDetailRepository) {
        this.cartRepository = cartRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.cartDetailRepository = cartDetailRepository;
    }

    public CartResponseDTO getCartByUsername(String username) {
        try {
            return customerRepository.findByUserUsername(username)
                    .map(customer -> {
                        return cartRepository.findCartByCustomerIdAndActiveCart(customer.getId(), "ACTIVE")
                                .map(cart -> new CartResponseDTO(
                                        cart.getCartState(),
                                        cart.getCartDetails().stream()
                                                .map(detail -> new CartResponseDTO.CartDetailsDTO(
                                                        detail.getQuantity(),
                                                        detail.getProduct().getProductPrice(),
                                                        detail.getProduct().getProductName(),
                                                        detail.getProduct().getProductDescription()))
                                                .toList()))
                                .orElseGet(CartResponseDTO::new);
                    })
                    .orElseThrow(() -> new RuntimeException("Customer not found for username: " + username));
        } catch (Exception e) {
            log.error("Error retrieving cart for username: {}", username, e);
            throw new CartServiceBusinessException("Error retrieving cart information" + e.getMessage());
        }
    }

    public void addProductToCart(String username, CartRequestDTO cartRequestDTO) {
        try {
            Customer customer = customerRepository.findByUserUsername(username)
                    .orElseThrow(() -> new RuntimeException("Customer not found for username: " + username));
            Product product = productRepository.findById(cartRequestDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found for ID: " + cartRequestDTO.getProductId()));
            Optional<Cart> cartByCustomerId = cartRepository.findCartByCustomerIdAndActiveCart(customer.getId(), "ACTIVE");

            if (cartByCustomerId.isPresent()) {
                Cart cart = cartByCustomerId.get();
                //Add new CartDetail to existing cart
                CartDetail cartDetail = new CartDetail();

                cartDetail.setQuantity(cartRequestDTO.getQuantity());
                cartDetail.setCart(cart);
                cartDetail.setProduct(product);

                cart.setCartDetails(
                        Stream.concat(cart.getCartDetails().stream(), Stream.of(cartDetail)).collect(Collectors.toList())
                );
                cartRepository.save(cart);
            } else {
                //Create new cart and add CartDetail
                Cart newCart = new Cart();
                newCart.setCustomer(customer);
                newCart.setCartState("ACTIVE");

                CartDetail cartDetail = new CartDetail();
                cartDetail.setQuantity(cartRequestDTO.getQuantity());
                cartDetail.setProduct(product);
                cartDetail.setCart(newCart);

                newCart.setCartDetails(Stream.of(cartDetail).collect(Collectors.toList()));
                cartRepository.save(newCart);
            }
        } catch (Exception e) {
            log.error("Error adding product to cart for username: {}", username, e);
            throw new CartServiceBusinessException("Error adding product to cart" + e.getMessage());
        }
    }

    public void updateProductInCart(String username, CartRequestDTO cartRequestDTO) {
        try {
            Customer customer = customerRepository.findByUserUsername(username)
                    .orElseThrow(() -> new RuntimeException("Customer not found for username: " + username));
            Product product = productRepository.findById(cartRequestDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found for ID: " + cartRequestDTO.getProductId()));
            Cart cart = cartRepository.findCartByCustomerIdAndActiveCart(customer.getId(), "ACTIVE")
                    .orElseThrow(() -> new RuntimeException("Cart not found for customer ID: " + customer.getId()));

            Optional<CartDetail> existingDetail = cart.getCartDetails().stream()
                    .filter(detail -> detail.getProduct().getId().equals(product.getId()))
                    .findFirst();

            if (existingDetail.isPresent()) {
                existingDetail.get().setQuantity(cartRequestDTO.getQuantity());
                cartRepository.save(cart);
            } else {
                throw new RuntimeException("Product not found in cart");
            }
        } catch (Exception e) {
            log.error("Error updating product in cart for username: {}", username, e);
            throw new CartServiceBusinessException("Error updating product in cart" + e.getMessage());
        }
    }

    public void removeProductFromCart(String username, Long productId) {
        try {
            Customer customer = customerRepository.findByUserUsername(username)
                    .orElseThrow(() -> new RuntimeException("Customer not found for username: " + username));
            Cart cart = cartRepository.findCartByCustomerIdAndActiveCart(customer.getId(), "ACTIVE")
                    .orElseThrow(() -> new RuntimeException("Cart not found for customer ID: " + customer.getId()));

            cart.setCartDetails(cart.getCartDetails().stream()
                    .filter(detail -> !detail.getProduct().getId().equals(productId))
                    .collect(Collectors.toList()));

            cartDetailRepository.deleteByProductId(productId);
            cartRepository.save(cart);
        } catch (Exception e) {
            log.error("Error removing product from cart for username: {}", username, e);
            throw new CartServiceBusinessException("Error removing product from cart" + e.getMessage());
        }
    }

    public void clearCart(String username) {
        try {
            Customer customer = customerRepository.findByUserUsername(username)
                    .orElseThrow(() -> new RuntimeException("Customer not found for username: " + username));
            Cart cart = cartRepository.findCartByCustomerIdAndActiveCart(customer.getId(), "ACTIVE")
                    .orElseThrow(() -> new RuntimeException("Cart not found for customer ID: " + customer.getId()));
            cartDetailRepository.deleteByCartId(cart.getId());
            cart.setCartDetails(null);
            cart.setCartState("INACTIVE");
            cartRepository.save(cart);
        } catch (Exception e) {
            log.error("Error clearing cart for username: {}", username, e);
            throw new CartServiceBusinessException("Error clearing cart" + e.getMessage());
        }
    }
}

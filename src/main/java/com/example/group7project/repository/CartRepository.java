package com.example.group7project.repository;

import com.example.group7project.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    // Additional query methods can be defined here if needed
    @Query("SELECT c FROM Cart c WHERE c.customer.id = ?1 and c.cartState = ?2")
    Optional<Cart> findCartByCustomerIdAndActiveCart(Long customerId, String cartState);
}

package com.example.group7project.repository;

import com.example.group7project.entity.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {
    @Modifying
    @Query("DELETE FROM CartDetail cd WHERE cd.product.id = ?1")
    void deleteByProductId(Long productId);

    @Modifying
@Query("DELETE FROM CartDetail cd WHERE cd.cart.id = ?1")
    void deleteByCartId(Long cartId);
}

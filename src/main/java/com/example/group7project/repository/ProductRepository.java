package com.example.group7project.repository;

import com.example.group7project.dto.ProductResponseDTO;
import com.example.group7project.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT new com.example.group7project.dto.ProductResponseDTO(p.id, p.productName, p.productDescription, p.productPrice, p.stockQuantity) " +
            "FROM Product p")
    List<ProductResponseDTO> findAllProducts();

    Optional<Product> findById(Long id);
}

package com.example.group7project.service;

import com.example.group7project.dto.ProductResponseDTO;
import com.example.group7project.entity.Product;
import com.example.group7project.exception.ProductServiceBusinessException;
import com.example.group7project.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponseDTO> getAllProducts() {
        try {
            return productRepository.findAllProducts();
        }
        catch (Exception e) {
            log.error("Error fetching products: {}", e.getMessage());
            throw new ProductServiceBusinessException("An error occurred while fetching products. Please try again later.");
        }

    }

    public ProductResponseDTO getProductById(Long id) {
        try {
            return productRepository.findById(id)
                    .map(product -> new ProductResponseDTO(
                            product.getId(),
                            product.getProductName(),
                            product.getProductDescription(),
                            product.getProductPrice(),
                            product.getStockQuantity()))
                    .orElseThrow(() -> new ProductServiceBusinessException("Product not found with ID: " + id));
        }
        catch (Exception e) {
            log.error("Error fetching product with ID {}: {}", id, e.getMessage());
            throw new ProductServiceBusinessException("An error occurred while fetching the product. Please try again later.");
        }
    }

    public void addProducts(List<ProductResponseDTO> products) {
        try {
            for (ProductResponseDTO productDTO : products) {
                Product product = new Product();
                product.setProductName(productDTO.getProductName());
                product.setProductDescription(productDTO.getProductDescription());
                product.setProductPrice(productDTO.getProductPrice());
                product.setStockQuantity(productDTO.getStockQuantity());

                productRepository.save(product);
            }
            log.info("Products added successfully.");
        } catch (Exception e) {
            log.error("Error adding products: {}", e.getMessage());
            throw new ProductServiceBusinessException("An error occurred while adding products. Please try again later.");
        }
    }

    public void updateProduct(Long id, ProductResponseDTO productDTO) {
        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new ProductServiceBusinessException("Product not found with ID: " + id));

            product.setProductName(productDTO.getProductName());
            product.setProductDescription(productDTO.getProductDescription());
            product.setProductPrice(productDTO.getProductPrice());
            product.setStockQuantity(productDTO.getStockQuantity());

            productRepository.save(product);
            log.info("Product with ID {} updated successfully.", id);
        } catch (Exception e) {
            log.error("Error updating product with ID {}: {}", id, e.getMessage());
            throw new ProductServiceBusinessException("An error occurred while updating the product. Please try again later.");
        }
    }
}

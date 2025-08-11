package com.example.group7project.controller;

import com.example.group7project.dto.AppResponseDTO;
import com.example.group7project.dto.ProductResponseDTO;
import com.example.group7project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public AppResponseDTO<List<ProductResponseDTO>> getAllProducts() {
        List<ProductResponseDTO> products = productService.getAllProducts();
        return new AppResponseDTO<>(HttpStatus.OK, products);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public AppResponseDTO<ProductResponseDTO> getProductById(@PathVariable Long id) {
        ProductResponseDTO product = productService.getProductById(id);
        return new AppResponseDTO<>(HttpStatus.OK, product);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public AppResponseDTO<String> addProducts(@RequestBody List<ProductResponseDTO> products) {
        productService.addProducts(products);
        return new AppResponseDTO<>(HttpStatus.CREATED, "Products added successfully");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public AppResponseDTO<String> updateProduct(@PathVariable Long id, @RequestBody ProductResponseDTO productDTO) {
        productService.updateProduct(id, productDTO);
        return new AppResponseDTO<>(HttpStatus.OK, "Product updated successfully");
    }
}

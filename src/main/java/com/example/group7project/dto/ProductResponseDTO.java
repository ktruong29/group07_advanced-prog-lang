package com.example.group7project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO implements Serializable {
    private Long id;
    private String productName;
    private String productDescription;
    private Double productPrice;
    private Integer stockQuantity;
}

package com.example.group7project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO implements Serializable {
    private Integer quantity;
    private Double unitPrice;
    private String productName;
    private String productDescription;
    private Double productPrice;
}

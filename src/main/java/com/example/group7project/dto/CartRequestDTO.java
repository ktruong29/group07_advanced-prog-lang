package com.example.group7project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartRequestDTO implements Serializable {
    private Long productId;
    private Integer quantity;
}

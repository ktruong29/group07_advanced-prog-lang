package com.example.group7project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseDTO implements Serializable {
    private String cartState;
    private List<CartDetailsDTO> cartDetails;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CartDetailsDTO implements Serializable {
        private Integer quantity;
        private Double productPrice;
        private String productName;
        private String productDescription;
    }
}

package com.example.group7project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO implements Serializable {
    private Long orderId;
    private String orderStatus;
    private LocalDateTime orderDate;
    private Double orderTotalAmount;
}

package com.example.group7project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponseDTO implements Serializable {
    private LocalDateTime paymentDate;
    private Double paymentAmount;
    private String paymentMethod;
    private Long orderId;
}

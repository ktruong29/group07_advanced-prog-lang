package com.example.group7project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO implements Serializable {
    private String paymentMethod;
    private Double paymentAmount;
}

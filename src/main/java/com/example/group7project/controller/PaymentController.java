package com.example.group7project.controller;

import com.example.group7project.dto.AppResponseDTO;
import com.example.group7project.dto.PaymentResponseDTO;
import com.example.group7project.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public AppResponseDTO<PaymentResponseDTO> getPaymentByUsernameAndId(@PathVariable Long orderId) {
        String usernameFromAuth = SecurityContextHolder.getContext().getAuthentication().getName();
        PaymentResponseDTO paymentResponseDTO = paymentService.getPaymentByUsernameAndId(usernameFromAuth, orderId);
        return new AppResponseDTO<>(HttpStatus.OK, paymentResponseDTO);
    }
}

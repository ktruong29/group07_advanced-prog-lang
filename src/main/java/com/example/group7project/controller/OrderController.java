package com.example.group7project.controller;

import com.example.group7project.dto.AppResponseDTO;
import com.example.group7project.dto.OrderDTO;
import com.example.group7project.dto.OrderDetailDTO;
import com.example.group7project.dto.PaymentRequestDTO;
import com.example.group7project.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/checkout")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public AppResponseDTO<String> checkout(@RequestBody PaymentRequestDTO paymentRequestDTO) {
        String usernameFromAuth = SecurityContextHolder.getContext().getAuthentication().getName();
        orderService.checkoutAndPlaceOrder(usernameFromAuth,paymentRequestDTO);
        return new AppResponseDTO<>(HttpStatus.OK, "Order placed successfully");
    }

    @GetMapping("/customer")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public AppResponseDTO<List<OrderDTO>> getOrders() {
        String usernameFromAuth = SecurityContextHolder.getContext().getAuthentication().getName();
        List<OrderDTO> orders = orderService.getOrdersByUsername(usernameFromAuth);
        return new AppResponseDTO<>(HttpStatus.OK, orders);
    }

    @GetMapping("/customer/{orderId}")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public AppResponseDTO<List<OrderDetailDTO>> getOrderDetails(@PathVariable Long orderId) {
        String usernameFromAuth = SecurityContextHolder.getContext().getAuthentication().getName();
        List<OrderDetailDTO> orderDetailsByUsername = orderService.getOrderDetailsByUsername(usernameFromAuth, orderId);
        return new AppResponseDTO<>(HttpStatus.OK, orderDetailsByUsername);
    }

    @GetMapping("/{customerId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public AppResponseDTO<List<OrderDTO>> getOrdersByCustomerId(@PathVariable Long customerId) {
        List<OrderDTO> orders = orderService.getOrdersByCustomerId(customerId);
        return new AppResponseDTO<>(HttpStatus.OK, orders);
    }
}

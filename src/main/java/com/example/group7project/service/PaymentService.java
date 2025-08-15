package com.example.group7project.service;

import com.example.group7project.dto.PaymentResponseDTO;
import com.example.group7project.entity.Customer;
import com.example.group7project.entity.Order;
import com.example.group7project.exception.PaymentServiceBusinessException;
import com.example.group7project.repository.CustomerRepository;
import com.example.group7project.repository.OrderRepository;
import com.example.group7project.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository,
                          CustomerRepository customerRepository,
                          OrderRepository orderRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    public PaymentResponseDTO getPaymentByUsernameAndId(String username, Long orderId) {
        try {
            Customer customer = customerRepository.findByUserUsername(username)
                    .orElseThrow(() -> new RuntimeException("Customer not found for username: " + username));
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found for ID: " + orderId));
            return  PaymentResponseDTO.builder()
                    .paymentAmount(order.getPayment().getPaymentAmount())
                    .paymentDate(order.getPayment().getPaymentDate())
                    .paymentMethod(order.getPayment().getPaymentMethod())
                    .orderId(order.getId())
                    .build();

        } catch (Exception e) {
            log.error("Error fetching payment for user: {} with paymentId: {}", username, orderId, e);
            throw new PaymentServiceBusinessException("Failed to fetch payment details" + e);
        }
    }
}

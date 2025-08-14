package com.example.group7project.service;

import com.example.group7project.dto.OrderDTO;
import com.example.group7project.dto.OrderDetailDTO;
import com.example.group7project.dto.PaymentRequestDTO;
import com.example.group7project.entity.*;
import com.example.group7project.exception.OrderServiceBusinessException;
import com.example.group7project.repository.CartRepository;
import com.example.group7project.repository.CustomerRepository;
import com.example.group7project.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@Transactional
public class OrderService {
    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(CustomerRepository customerRepository,
                        CartRepository cartRepository,
                        OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
    }

    public void checkoutAndPlaceOrder(String username, PaymentRequestDTO paymentRequestDTO) {
        try {
            Customer customer = customerRepository.findByUserUsername(username)
                    .orElseThrow(() -> new RuntimeException("Customer not found for username: " + username));
            Cart cart = cartRepository.findCartByCustomerIdAndActiveCart(customer.getId(), "ACTIVE")
                    .orElseThrow(() -> new RuntimeException("Cart not found for customer ID: " + customer.getId()));

            List<OrderDetail> orderDetails = getOrderDetails(cart);
            Payment payment = getPayment(paymentRequestDTO);

            Order order = new Order();

            order.setOrderDate(LocalDateTime.now());
            order.setOrderStatus("COMPLETED");
            Double totalAmount = orderDetails.stream()
                    .map(orderDetail -> orderDetail.getQuantity() * orderDetail.getUnitPrice())
                    .reduce(0.0, Double::sum);
            order.setTotalAmount(totalAmount);
            order.setOrderDetails(orderDetails);
            order.setCustomer(customer);

            // Set order reference in each OrderDetail
            for (OrderDetail detail : orderDetails) {
                detail.setOrder(order);
            }
            order.setOrderDetails(orderDetails);

            payment.setOrder(order);
            order.setPayment(payment);

            orderRepository.save(order);

            //Set cart state to INACTIVE after placing the order
            cart.setCartState("INACTIVE");
            cartRepository.save(cart);
        }
        catch (Exception e) {
            log.error("Error during checkout and placing order for user: {}", username, e);
            throw new OrderServiceBusinessException("Checkout failed for user: " + username, e);
        }
    }

    private static Payment getPayment(PaymentRequestDTO paymentRequestDTO) {
        Payment payment = new Payment();
        payment.setPaymentMethod(paymentRequestDTO.getPaymentMethod());
        payment.setPaymentAmount(payment.getPaymentAmount());
        payment.setPaymentDate(LocalDateTime.now());
        return payment;
    }

    private static List<OrderDetail> getOrderDetails(Cart cart) {
        return cart.getCartDetails()
                .stream()
                .map(cartDetail -> {
                    OrderDetail orderDetail = new OrderDetail();

                    orderDetail.setQuantity(cartDetail.getQuantity());
                    orderDetail.setUnitPrice(cartDetail.getProduct().getProductPrice());
                    orderDetail.setProduct(cartDetail.getProduct());

                    return orderDetail;
                })
                .toList();
    }

    public List<OrderDTO> getOrdersByUsername(String username) {
        try {
            Customer customer = customerRepository.findByUserUsername(username)
                    .orElseThrow(() -> new RuntimeException("Customer not found for username: " + username));
            List<Order> orders = customer.getOrders();
            return orders.stream()
                    .map(order -> new OrderDTO(order.getId(), order.getOrderStatus(), order.getOrderDate(), order.getTotalAmount()))
                    .toList();
        } catch (Exception e) {
            log.error("Error retrieving orders for username: {}", username, e);
            throw new OrderServiceBusinessException("Error retrieving orders for user: " + username, e);
        }
    }

    public List<OrderDetailDTO> getOrderDetailsByUsername(String username, Long orderId) {
        try {
            Customer customer = customerRepository.findByUserUsername(username)
                    .orElseThrow(() -> new RuntimeException("Customer not found for username: " + username));
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found for ID: " + orderId));
            if (!order.getCustomer().getId().equals(customer.getId())) {
                throw new RuntimeException("Order does not belong to the user: " + username);
            }
            return order.getOrderDetails()
                    .stream()
                    .map(detail -> new OrderDetailDTO(
                            detail.getQuantity(),
                            detail.getUnitPrice(),
                            detail.getProduct().getProductName(),
                            detail.getProduct().getProductDescription(),
                            detail.getProduct().getProductPrice()))
                    .toList();
        } catch (Exception e) {
            log.error("Error retrieving order details for username: {} and order ID: {}", username, orderId, e);
            throw new OrderServiceBusinessException("Error retrieving order details for user: " + username, e);
        }
    }

    public List<OrderDTO> getOrdersByCustomerId(Long customerId) {
        try {
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found for ID: " + customerId));
            List<Order> orders = customer.getOrders();
            return orders.stream()
                    .map(order -> new OrderDTO(order.getId(), order.getOrderStatus(), order.getOrderDate(), order.getTotalAmount()))
                    .toList();
        } catch (Exception e) {
            log.error("Error retrieving orders for customer ID: {}", customerId, e);
            throw new OrderServiceBusinessException("Error retrieving orders for customer ID: " + customerId, e);
        }
    }

}

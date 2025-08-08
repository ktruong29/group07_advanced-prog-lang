package com.example.group7project.service;

import com.example.group7project.dto.UserCustomerDTO;
import com.example.group7project.entity.Customer;
import com.example.group7project.exception.CustomerServiceBusinessException;
import com.example.group7project.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public UserCustomerDTO getCustomerByUsername(String username) {
        try {
            return customerRepository.findUserCustomerByUsername(username)
                    .map(customer -> new UserCustomerDTO(
                            customer.getFirstName(),
                            customer.getLastName(),
                            customer.getEmail(),
                            customer.getPhoneNumber(),
                            customer.getAddress()))
                    .orElseThrow(() -> new RuntimeException("Customer not found for username: " + username));
        }
        catch (Exception e) {
            log.error("Error retrieving customer for username: {}", username, e);
            throw new CustomerServiceBusinessException("Error retrieving customer information", e);
        }
    }

    public void updateCustomerInfo(String username, UserCustomerDTO updatedInfo) {
        try {
            Customer customer = customerRepository.findByUserUsername(username)
                    .orElseThrow(() -> new RuntimeException("Customer not found for username: " + username));

            customer.setFirstName(updatedInfo.getFirstName());
            customer.setLastName(updatedInfo.getLastName());
            customer.setPhoneNumber(updatedInfo.getPhoneNumber());
            customer.setAddress(updatedInfo.getAddress());

            customerRepository.save(customer);
        }
        catch (Exception e) {
            log.error("Error updating customer for username: {}", username, e);
            throw new CustomerServiceBusinessException("Error updating customer information", e);
        }
    }
}

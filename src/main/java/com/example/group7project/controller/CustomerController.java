package com.example.group7project.controller;

import com.example.group7project.dto.AppResponseDTO;
import com.example.group7project.dto.UserCustomerDTO;
import com.example.group7project.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/self")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public AppResponseDTO<UserCustomerDTO> getCustomerByUsername() {
        String usernameFromAuth = SecurityContextHolder.getContext().getAuthentication().getName();
        UserCustomerDTO customer = customerService.getCustomerByUsername(usernameFromAuth);
        return new AppResponseDTO<>(HttpStatus.OK, customer);
    }

    @PutMapping()
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public AppResponseDTO<String> updateCustomerInfo(@RequestBody UserCustomerDTO updatedInfo) {
        String usernameFromAuth = SecurityContextHolder.getContext().getAuthentication().getName();
        customerService.updateCustomerInfo(usernameFromAuth, updatedInfo);
        return new AppResponseDTO<>(HttpStatus.OK, "Customer information updated successfully");
    }
}

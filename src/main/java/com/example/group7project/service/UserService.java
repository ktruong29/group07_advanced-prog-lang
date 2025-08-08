package com.example.group7project.service;

import com.example.group7project.dto.UserResponseDTO;
import com.example.group7project.dto.UserRequestDTO;
import com.example.group7project.entity.Customer;
import com.example.group7project.entity.User;
import com.example.group7project.exception.UserServiceBusinessException;
import com.example.group7project.repository.CustomerRepository;
import com.example.group7project.repository.RoleRepository;
import com.example.group7project.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService {
    public static final String DEFAULT_ROLE = "ROLE_USER";

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository,
                       CustomerRepository customerRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.customerRepository = customerRepository;
    }

    public void createNewUser(UserRequestDTO userDto) {
        Customer customer = new Customer();
        customer.setFirstName(userDto.getFirstName());
        customer.setLastName(userDto.getLastName());
        customer.setPhoneNumber(userDto.getPhoneNumber());
        customer.setAddress(userDto.getAddress());

        User newUser = new User();
        newUser.setUsername(userDto.getUsername());
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        newUser.setEmail(userDto.getEmail());
        newUser.setRole(roleRepository.findByRole(DEFAULT_ROLE));
        newUser.setCustomer(customer);

        customer.setUser(newUser);
        userRepository.save(newUser);
        log.info("New user created: {}", newUser.getUsername());
    }

    public UserResponseDTO getUserById(Long id) {
        try {
            return userRepository.findUserResponseById(id)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        }
        catch (Exception e) {
            log.error("UserService::getUserById method execution failed: {}", e.getMessage());
            throw new UserServiceBusinessException("UserService::getUserById method execution failed: " + e.getMessage(), e);
        }
    }

    public List<UserResponseDTO> getAllUsers() {
        try {
            return userRepository.findAllUserResponse()
                    .orElseThrow(() -> new RuntimeException("No users found") );
        }
        catch (Exception e) {
            log.error("UserService::getAllUsers method execution failed: {}", e.getMessage());
            throw new UserServiceBusinessException("UserService::getAllUsers method execution failed: " + e.getMessage(), e);
        }
    }
}

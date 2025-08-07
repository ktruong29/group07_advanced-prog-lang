package com.example.group7project.service;

import com.example.group7project.dto.UserDTO;
import com.example.group7project.entity.Customer;
import com.example.group7project.entity.User;
import com.example.group7project.repository.CustomerRepository;
import com.example.group7project.repository.RoleRepository;
import com.example.group7project.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public void createNewUser(UserDTO userDto) {
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
}

package com.example.group7project.repository;

import com.example.group7project.dto.UserCustomerDTO;
import com.example.group7project.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT new com.example.group7project.dto.UserCustomerDTO(c.firstName, c.lastName, c.user.email, c.phoneNumber, c.address) " +
           "FROM Customer c WHERE c.user.username = ?1")
    Optional<UserCustomerDTO> findUserCustomerByUsername(String username);

    @Query("FROM Customer c WHERE c.user.username = ?1")
    Optional<Customer> findByUserUsername(String username);
}

package com.example.group7project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCustomerDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
}

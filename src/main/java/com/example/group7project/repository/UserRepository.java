package com.example.group7project.repository;

import com.example.group7project.dto.UserResponseDTO;
import com.example.group7project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query("SELECT new com.example.group7project.dto.UserResponseDTO(u.username, u.email, u.role.role) " +
           "FROM User u WHERE u.id = ?1")
    Optional<UserResponseDTO> findUserResponseById(Long id);

    @Query("SELECT new com.example.group7project.dto.UserResponseDTO(u.username, u.email, u.role.role) " +
            "FROM User u")
    Optional<List<UserResponseDTO>> findAllUserResponse();
}

package com.example.group7project.handler;

import com.example.group7project.dto.AppResponseDTO;
import com.example.group7project.dto.ErrorDTO;
import com.example.group7project.exception.CartServiceBusinessException;
import com.example.group7project.exception.CustomerServiceBusinessException;
import com.example.group7project.exception.ProductServiceBusinessException;
import com.example.group7project.exception.UserServiceBusinessException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ExpiredJwtException.class)
    public AppResponseDTO<?> handleExpiredJwtException(ExpiredJwtException ex) {
        AppResponseDTO<?> appResponseDTO = new AppResponseDTO<>();

        List<ErrorDTO> errors = List.of(new ErrorDTO("Token has expired. Please log in again."));

        appResponseDTO.setStatus(HttpStatus.UNAUTHORIZED);
        appResponseDTO.setErrors(errors);

        return appResponseDTO;
    }

    @ExceptionHandler(UserServiceBusinessException.class)
    public AppResponseDTO<?> handleUserServiceBusinessException(UserServiceBusinessException ex) {
        AppResponseDTO<?> appResponseDTO = new AppResponseDTO<>();

        List<ErrorDTO> errors = List.of(new ErrorDTO(ex.getMessage()));

        appResponseDTO.setStatus(HttpStatus.BAD_REQUEST);
        appResponseDTO.setErrors(errors);

        return appResponseDTO;
    }

    @ExceptionHandler(CustomerServiceBusinessException.class)
    public AppResponseDTO<?> handleCustomerServiceBusinessException(CustomerServiceBusinessException ex) {
        AppResponseDTO<?> appResponseDTO = new AppResponseDTO<>();

        List<ErrorDTO> errors = List.of(new ErrorDTO(ex.getMessage()));

        appResponseDTO.setStatus(HttpStatus.BAD_REQUEST);
        appResponseDTO.setErrors(errors);

        return appResponseDTO;
    }

    @ExceptionHandler(ProductServiceBusinessException.class)
    public AppResponseDTO<?> handleProductServiceBusinessException(ProductServiceBusinessException ex) {
        AppResponseDTO<?> appResponseDTO = new AppResponseDTO<>();

        List<ErrorDTO> errors = List.of(new ErrorDTO(ex.getMessage()));

        appResponseDTO.setStatus(HttpStatus.BAD_REQUEST);
        appResponseDTO.setErrors(errors);

        return appResponseDTO;
    }

    @ExceptionHandler(CartServiceBusinessException.class)
    public AppResponseDTO<?> handleCartServiceBusinessException(CartServiceBusinessException ex) {
        AppResponseDTO<?> appResponseDTO = new AppResponseDTO<>();

        List<ErrorDTO> errors = List.of(new ErrorDTO(ex.getMessage()));

        appResponseDTO.setStatus(HttpStatus.BAD_REQUEST);
        appResponseDTO.setErrors(errors);

        return appResponseDTO;
    }
}

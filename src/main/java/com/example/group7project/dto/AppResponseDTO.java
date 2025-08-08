package com.example.group7project.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppResponseDTO<T> {
    private HttpStatus status;
    private T payload;
    private List<ErrorDTO> errors;

    public AppResponseDTO(HttpStatus status, T payload) {
        this.payload = payload;
        this.status = status;
    }
}

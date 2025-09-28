package com.backend.rest_api.domain.dto;

import lombok.Data;

@Data
public class UserResponseDTO {
    private Integer id;
    private String name;
    private String email;
}

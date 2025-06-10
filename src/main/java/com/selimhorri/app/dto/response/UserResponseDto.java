package com.selimhorri.app.dto.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer userId;
    private String firstName;
    private String lastName;
    private String imageUrl;
    private String email;
    private String phone;
    private CredentialResponseDto credential; // Anidado para mostrar información relacionada

}
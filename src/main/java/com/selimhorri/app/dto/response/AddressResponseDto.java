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
public class AddressResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer addressId;
    private String fullAddress;
    private String postalCode;
    private String city;
    private UserResponseDto user; // Reutilizamos el DTO de respuesta del usuario

}
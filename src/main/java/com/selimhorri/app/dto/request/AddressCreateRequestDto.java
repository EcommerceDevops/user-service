package com.selimhorri.app.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressCreateRequestDto {

    @NotBlank(message = "Full address cannot be blank")
    private String fullAddress;

    @NotBlank(message = "Postal code cannot be blank")
    private String postalCode;

    @NotBlank(message = "City cannot be blank")
    private String city;

    @NotNull(message = "User ID cannot be null")
    private Integer userId; // Solo necesitamos el ID del propietario

}
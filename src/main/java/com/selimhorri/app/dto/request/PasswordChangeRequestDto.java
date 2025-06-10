package com.selimhorri.app.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeRequestDto {

    @NotBlank(message = "Password cannot be blank")
    private String password;

}
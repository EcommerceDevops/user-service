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
public class CredentialInfoUpdateRequestDto {

    @NotBlank(message = "Role cannot be blank")
    private String roleBasedAuthority;

    @NotNull(message = "isEnabled status cannot be null")
    private Boolean isEnabled;

    @NotNull(message = "isAccountNonExpired status cannot be null")
    private Boolean isAccountNonExpired;

    @NotNull(message = "isAccountNonLocked status cannot be null")
    private Boolean isAccountNonLocked;

    @NotNull(message = "isCredentialsNonExpired status cannot be null")
    private Boolean isCredentialsNonExpired;

}
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
public class CredentialResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer credentialId;
    private String username;
    private String roleBasedAuthority;
    private Boolean isEnabled;
    private Boolean isAccountNonExpired;
    private Boolean isAccountNonLocked;
    private Boolean isCredentialsNonExpired;
    private UserResponseDto user;
}
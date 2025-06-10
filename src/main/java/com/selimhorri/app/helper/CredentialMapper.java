package com.selimhorri.app.helper;

import com.selimhorri.app.domain.Credential;
import com.selimhorri.app.domain.RoleBasedAuthority;
import com.selimhorri.app.dto.request.CredentialInfoUpdateRequestDto;
import com.selimhorri.app.dto.response.CredentialResponseDto;

public final class CredentialMapper {

	private CredentialMapper() {
	}

	/**
	 * Maps a Credential entity to a detailed CredentialResponseDto,
	 * including the associated user information.
	 */
	public static CredentialResponseDto toResponseDto(final Credential credential) {
		return CredentialResponseDto.builder()
				.credentialId(credential.getCredentialId())
				.username(credential.getUsername())
				.roleBasedAuthority(credential.getRoleBasedAuthority().name())
				.isEnabled(credential.getIsEnabled())
				.isAccountNonExpired(credential.getIsAccountNonExpired())
				.isAccountNonLocked(credential.getIsAccountNonLocked())
				.isCredentialsNonExpired(credential.getIsCredentialsNonExpired())
				.user(UserMapper.toResponseDto(credential.getUser())) // Reutiliza el UserMapper
				.build();
	}

	/**
	 * Updates a Credential entity with data from a CredentialInfoUpdateRequestDto.
	 * This does NOT update the password.
	 */
	public static void updateEntityFromDto(final Credential credential, final CredentialInfoUpdateRequestDto dto) {
		credential.setRoleBasedAuthority(RoleBasedAuthority.valueOf(dto.getRoleBasedAuthority()));
		credential.setIsEnabled(dto.getIsEnabled());
		credential.setIsAccountNonExpired(dto.getIsAccountNonExpired());
		credential.setIsAccountNonLocked(dto.getIsAccountNonLocked());
		credential.setIsCredentialsNonExpired(dto.getIsCredentialsNonExpired());
	}

}
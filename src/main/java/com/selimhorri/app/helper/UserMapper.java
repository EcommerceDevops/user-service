package com.selimhorri.app.helper;

import com.selimhorri.app.domain.Credential;
import com.selimhorri.app.domain.RoleBasedAuthority;
import com.selimhorri.app.domain.User;
import com.selimhorri.app.dto.request.UserCreateRequestDto;
import com.selimhorri.app.dto.request.UserUpdateRequestDto;
import com.selimhorri.app.dto.response.CredentialResponseDto; // Asumiendo que tienes este
import com.selimhorri.app.dto.response.UserResponseDto;
import java.time.Instant; // <-- IMPORTANTE

public final class UserMapper {

	private UserMapper() {
	}

	public static UserResponseDto toResponseDto(final User user) {
		// ... (tu código existente para mapear a UserResponseDto)
		final Credential credential = user.getCredential();
		final CredentialResponseDto credentialDto = CredentialResponseDto.builder() // O tu CredentialSimpleResponseDto
				.credentialId(credential.getCredentialId())
				.username(credential.getUsername())
				.roleBasedAuthority(credential.getRoleBasedAuthority().getRole()) // o .name()
				.isEnabled(credential.getIsEnabled())
				.isAccountNonExpired(credential.getIsAccountNonExpired())
				.isAccountNonLocked(credential.getIsAccountNonLocked())
				.isCredentialsNonExpired(credential.getIsCredentialsNonExpired())
				.build();

		return UserResponseDto.builder()
				.userId(user.getUserId())
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.imageUrl(user.getImageUrl())
				.email(user.getEmail())
				.phone(user.getPhone())
				.credential(credentialDto)
				.build();
	}

	public static User toEntity(final UserCreateRequestDto createDto) {

		final User user = new User();
		user.setFirstName(createDto.getFirstName());
		user.setLastName(createDto.getLastName());
		user.setEmail(createDto.getEmail());
		user.setPhone(createDto.getPhone());

		final Credential credential = new Credential();
		credential.setUsername(createDto.getUsername());
		credential.setPassword(createDto.getPassword()); // Recuerda que esto se codifica en el servicio
		credential.setRoleBasedAuthority(RoleBasedAuthority.ROLE_USER);
		credential.setIsEnabled(true);
		credential.setIsAccountNonExpired(true);
		credential.setIsAccountNonLocked(true);
		credential.setIsCredentialsNonExpired(true);

		// ==================================================================
		// ===== INYECCIÓN MANUAL DE TIMESTAMPS PARA CREDENTIAL ===========
		final Instant now = Instant.now();
		credential.setCreatedAt(now);
		credential.setUpdatedAt(now);
		// ==================================================================

		user.setCredential(credential);
		credential.setUser(user);

		return user;
	}

	public static void updateEntityFromDto(final User user, final UserUpdateRequestDto updateDto) {
		user.setFirstName(updateDto.getFirstName());
		user.setLastName(updateDto.getLastName());
		user.setImageUrl(updateDto.getImageUrl());
		user.setEmail(updateDto.getEmail());
		user.setPhone(updateDto.getPhone());
	}

}
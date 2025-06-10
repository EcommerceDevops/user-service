package com.selimhorri.app.service;

import java.util.List;
import com.selimhorri.app.dto.request.CredentialInfoUpdateRequestDto;
import com.selimhorri.app.dto.request.PasswordChangeRequestDto;
import com.selimhorri.app.dto.response.CredentialResponseDto;

public interface CredentialService {

	List<CredentialResponseDto> findAll();

	CredentialResponseDto findById(final Integer credentialId);

	CredentialResponseDto findByUsername(final String username);

	CredentialResponseDto updateInfo(final Integer credentialId, final CredentialInfoUpdateRequestDto dto);

	CredentialResponseDto updatePassword(final Integer credentialId, final PasswordChangeRequestDto dto);

	// Métodos que no deberían ser soportados
	void create();

	void deleteById(final Integer credentialId);

}
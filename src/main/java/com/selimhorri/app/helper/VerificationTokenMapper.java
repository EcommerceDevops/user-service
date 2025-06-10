package com.selimhorri.app.helper;

import com.selimhorri.app.domain.VerificationToken;
import com.selimhorri.app.dto.response.VerificationTokenResponseDto;

public final class VerificationTokenMapper {

	private VerificationTokenMapper() {
	}

	/**
	 * Maps a VerificationToken entity to a VerificationTokenResponseDto.
	 * It reuses the CredentialMapper to provide full context.
	 */
	public static VerificationTokenResponseDto toResponseDto(final VerificationToken verificationToken) {
		return VerificationTokenResponseDto.builder()
				.verificationTokenId(verificationToken.getVerificationTokenId())
				.token(verificationToken.getToken())
				.expireDate(verificationToken.getExpireDate())
				.credential(CredentialMapper.toResponseDto(verificationToken.getCredential())) // Reutilización
				.build();
	}

}
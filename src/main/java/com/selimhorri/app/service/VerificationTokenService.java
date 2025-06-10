package com.selimhorri.app.service;

import java.util.List;
import com.selimhorri.app.dto.response.VerificationTokenResponseDto;

public interface VerificationTokenService {

	// Operaciones de lectura (potencialmente solo para administradores)
	List<VerificationTokenResponseDto> findAll();

	VerificationTokenResponseDto findById(final Integer verificationTokenId);

	// Operaciones de mutación (no soportadas a través de la API pública)
	void create();

	void update();

	void deleteById(final Integer verificationTokenId);

}
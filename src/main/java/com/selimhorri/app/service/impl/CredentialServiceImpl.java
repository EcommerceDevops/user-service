package com.selimhorri.app.service.impl;

import java.time.Instant; // IMPORTANTE
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.selimhorri.app.domain.Credential;
import com.selimhorri.app.dto.request.CredentialInfoUpdateRequestDto;
import com.selimhorri.app.dto.request.PasswordChangeRequestDto;
import com.selimhorri.app.dto.response.CredentialResponseDto;
import com.selimhorri.app.exception.wrapper.CredentialNotFoundException;
import com.selimhorri.app.helper.CredentialMapper;
import com.selimhorri.app.repository.CredentialRepository;
import com.selimhorri.app.service.CredentialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CredentialServiceImpl implements CredentialService {

	private final CredentialRepository credentialRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public List<CredentialResponseDto> findAll() {
		log.info("*** CredentialResponseDto List, service; fetch all credentials *");
		return this.credentialRepository.findAll()
				.stream()
				.map(CredentialMapper::toResponseDto)
				.collect(Collectors.toUnmodifiableList());
	}

	@Override
	public CredentialResponseDto findById(final Integer credentialId) {
		log.info("*** CredentialResponseDto, service; fetch credential by id *");
		return this.credentialRepository.findById(credentialId)
				.map(CredentialMapper::toResponseDto)
				.orElseThrow(() -> new CredentialNotFoundException(
						String.format("Credential with id: %d not found!", credentialId)));
	}

	@Override
	public CredentialResponseDto findByUsername(final String username) {
		log.info("*** CredentialResponseDto, service; fetch credential by username *");
		return this.credentialRepository.findByUsername(username)
				.map(CredentialMapper::toResponseDto)
				.orElseThrow(() -> new CredentialNotFoundException(
						String.format("Credential with username: %s not found!", username)));
	}

	@Override
	public CredentialResponseDto updateInfo(final Integer credentialId, final CredentialInfoUpdateRequestDto dto) {
		log.info("*** CredentialResponseDto, service; update credential info for id: {} *", credentialId);
		final Credential credential = this.credentialRepository.findById(credentialId)
				.orElseThrow(() -> new CredentialNotFoundException(
						String.format("Credential with id: %d not found!", credentialId)));

		CredentialMapper.updateEntityFromDto(credential, dto);
		credential.setUpdatedAt(Instant.now()); // Inyección manual de timestamp
		return CredentialMapper.toResponseDto(this.credentialRepository.save(credential));
	}

	@Override
	public CredentialResponseDto updatePassword(final Integer credentialId, final PasswordChangeRequestDto dto) {
		log.info("*** CredentialResponseDto, service; update password for credential id: {} *", credentialId);
		final Credential credential = this.credentialRepository.findById(credentialId)
				.orElseThrow(() -> new CredentialNotFoundException(
						String.format("Credential with id: %d not found!", credentialId)));

		credential.setPassword(this.passwordEncoder.encode(dto.getPassword()));
		credential.setUpdatedAt(Instant.now()); // Inyección manual de timestamp
		return CredentialMapper.toResponseDto(this.credentialRepository.save(credential));
	}

	@Override
	public void create() {
		throw new UnsupportedOperationException(
				"Creating a standalone credential is not allowed. Credentials must be created with a new user via POST /api/users.");
	}

	@Override
	public void deleteById(final Integer credentialId) {
		throw new UnsupportedOperationException(
				"Deleting a credential is not allowed. Delete the associated user instead via DELETE /api/users/{userId}.");
	}

}
package com.selimhorri.app.resource;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.selimhorri.app.dto.request.CredentialInfoUpdateRequestDto;
import com.selimhorri.app.dto.request.PasswordChangeRequestDto;
import com.selimhorri.app.dto.response.CredentialResponseDto;
import com.selimhorri.app.dto.response.collection.DtoCollectionResponse;
import com.selimhorri.app.service.CredentialService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = { "/api/credentials" })
@Slf4j
@RequiredArgsConstructor
public class CredentialResource {

	private final CredentialService credentialService;

	@GetMapping
	public ResponseEntity<DtoCollectionResponse<CredentialResponseDto>> findAll() {
		log.info("*** CredentialResponseDto List, controller; fetch all credentials *");
		return ResponseEntity.ok(new DtoCollectionResponse<>(this.credentialService.findAll()));
	}

	@GetMapping("/{credentialId}")
	public ResponseEntity<CredentialResponseDto> findById(
			@PathVariable("credentialId") @NotBlank(message = "Input must not be blank") final String credentialId) {
		log.info("*** CredentialResponseDto, resource; fetch credential by id *");
		return ResponseEntity.ok(this.credentialService.findById(Integer.parseInt(credentialId.strip())));
	}

	@GetMapping("/username/{username}")
	public ResponseEntity<CredentialResponseDto> findByUsername(
			@PathVariable("username") @NotBlank(message = "Input must not be blank") final String username) {
		log.info("*** CredentialResponseDto, resource; fetch credential by username *");
		return ResponseEntity.ok(this.credentialService.findByUsername(username));
	}

	@PostMapping
	public ResponseEntity<Void> create() {
		log.error("*** POST /api/credentials is not allowed! ***");
		// This endpoint is not allowed, as per the original code.
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build(); // Should not be reached
	}

	@PutMapping("/{credentialId}/info")
	public ResponseEntity<CredentialResponseDto> updateInfo(
			@PathVariable("credentialId") @NotBlank(message = "Input must not be blank") final String credentialId,
			@RequestBody @NotNull(message = "Input must not be NULL") @Valid final CredentialInfoUpdateRequestDto dto) {
		log.info("*** CredentialResponseDto, resource; update credential info with id {} ***", credentialId);
		return ResponseEntity.ok(this.credentialService.updateInfo(Integer.parseInt(credentialId.strip()), dto));
	}

	@PutMapping("/{credentialId}/password")
	public ResponseEntity<CredentialResponseDto> updatePassword(
			@PathVariable("credentialId") @NotBlank(message = "Input must not be blank") final String credentialId,
			@RequestBody @NotNull(message = "Input must not be NULL") @Valid final PasswordChangeRequestDto dto) {
		log.info("*** CredentialResponseDto, resource; update password for credential with id {} ***", credentialId);
		return ResponseEntity.ok(this.credentialService.updatePassword(Integer.parseInt(credentialId.strip()), dto));
	}

	@DeleteMapping("/{credentialId}")
	public ResponseEntity<Void> deleteById(@PathVariable("credentialId") final String credentialId) {
		log.error("*** DELETE /api/credentials/{id} is not allowed! ***");
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build(); // Should not be reached
	}

	// El endpoint PUT original en la raíz no es RESTful para estas operaciones.
	// Se ha omitido en favor de los endpoints /info y /password más específicos.
	// Para mantener la cantidad de endpoints, podrías dejar uno que devuelva
	// METHOD_NOT_ALLOWED.
	@PutMapping
	public ResponseEntity<Void> updateRoot() {
		log.error(
				"*** PUT /api/credentials is not a valid endpoint. Use PUT /api/credentials/{id}/info or /password instead. ***");
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
	}

	@PutMapping("/{credentialId}")
	public ResponseEntity<Void> updateByIdRoot() {
		log.error(
				"*** PUT /api/credentials/{id} is not a valid endpoint. Use PUT /api/credentials/{id}/info or /password instead. ***");
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
	}
}
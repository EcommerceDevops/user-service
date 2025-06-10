package com.selimhorri.app.resource;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.selimhorri.app.dto.response.VerificationTokenResponseDto;
import com.selimhorri.app.dto.response.collection.DtoCollectionResponse;
import com.selimhorri.app.service.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = { "/api/verificationTokens" })
@Slf4j
@RequiredArgsConstructor
public class VerificationTokenResource {

	private final VerificationTokenService verificationTokenService;

	/**
	 * WARNING: This endpoint exposes all verification tokens and is a security
	 * risk.
	 * It should be removed or secured for ADMIN users only.
	 */
	@GetMapping
	public ResponseEntity<DtoCollectionResponse<VerificationTokenResponseDto>> findAll() {
		log.info("*** VerificationTokenDto List, controller; fetch all verificationTokens *");
		return ResponseEntity.ok(new DtoCollectionResponse<>(this.verificationTokenService.findAll()));
	}

	/**
	 * WARNING: This endpoint exposes verification tokens by ID and is a security
	 * risk.
	 * It should be removed or secured for ADMIN users only.
	 */
	@GetMapping("/{verificationTokenId}")
	public ResponseEntity<VerificationTokenResponseDto> findById(
			@PathVariable("verificationTokenId") @NotBlank(message = "Input must not be blank") @Valid final String verificationTokenId) {
		log.info("*** VerificationTokenDto, resource; fetch verificationToken by id *");
		return ResponseEntity.ok(this.verificationTokenService.findById(Integer.parseInt(verificationTokenId.strip())));
	}

	@PostMapping
	public ResponseEntity<Void> create() {
		log.error("*** POST /api/verificationTokens is not allowed! ***");
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build(); // Should not be reached
	}

	@PutMapping
	public ResponseEntity<Void> update() {
		log.error("*** PUT /api/verificationTokens is not allowed! ***");
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build(); // Should not be reached
	}

	@PutMapping("/{verificationTokenId}")
	public ResponseEntity<Void> updateById(@PathVariable("verificationTokenId") final String verificationTokenId) {
		log.error("*** PUT /api/verificationTokens/{id} is not allowed! ***");
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build(); // Should not be reached
	}

	@DeleteMapping("/{verificationTokenId}")
	public ResponseEntity<Void> deleteById(
			@PathVariable("verificationTokenId") @NotBlank(message = "Input must not be blank") final String verificationTokenId) {
		log.error("*** DELETE /api/verificationTokens/{id} is not allowed! ***");
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build(); // Should not be reached
	}

}
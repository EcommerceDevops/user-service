package com.selimhorri.app.resource;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.selimhorri.app.dto.request.AddressCreateRequestDto;
import com.selimhorri.app.dto.request.AddressUpdateRequestDto;
import com.selimhorri.app.dto.response.AddressResponseDto;
import com.selimhorri.app.dto.response.collection.DtoCollectionResponse;
import com.selimhorri.app.service.AddressService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = { "/api/address" })
@Slf4j
@RequiredArgsConstructor
public class AddressResource {

	private final AddressService addressService;

	@GetMapping
	public ResponseEntity<DtoCollectionResponse<AddressResponseDto>> findAll() {
		log.info("*** AddressResponseDto List, controller; fetch all addresses *");
		return ResponseEntity.ok(new DtoCollectionResponse<>(this.addressService.findAll()));
	}

	@GetMapping("/{addressId}")
	public ResponseEntity<AddressResponseDto> findById(
			@PathVariable("addressId") @NotBlank(message = "Input must not be blank") final String addressId) {
		log.info("*** AddressResponseDto, resource; fetch address by id *");
		return ResponseEntity.ok(this.addressService.findById(Integer.parseInt(addressId.strip())));
	}

	@PostMapping
	public ResponseEntity<AddressResponseDto> create(
			@RequestBody @NotNull(message = "Input must not be NULL") @Valid final AddressCreateRequestDto createDto) {
		log.info("*** AddressResponseDto, resource; create address *");
		final AddressResponseDto createdAddress = this.addressService.create(createDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdAddress);
	}

	@PutMapping("/{addressId}")
	public ResponseEntity<AddressResponseDto> update(
			@PathVariable("addressId") @NotBlank(message = "Input must not be blank") final String addressId,
			@RequestBody @NotNull(message = "Input must not be NULL") @Valid final AddressUpdateRequestDto updateDto) {
		log.info("*** AddressResponseDto, resource; update address with addressId *");
		return ResponseEntity.ok(this.addressService.update(Integer.parseInt(addressId.strip()), updateDto));
	}

	@DeleteMapping("/{addressId}")
	public ResponseEntity<Void> deleteById(
			@PathVariable("addressId") @NotBlank(message = "Input must not be blank") final String addressId) {
		log.info("*** Void, resource; delete address by id *");
		this.addressService.deleteById(Integer.parseInt(addressId.strip()));
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
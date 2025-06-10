package com.selimhorri.app.resource;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.selimhorri.app.dto.request.UserCreateRequestDto;
import com.selimhorri.app.dto.request.UserUpdateRequestDto;
import com.selimhorri.app.dto.response.UserResponseDto;
import com.selimhorri.app.dto.response.collection.DtoCollectionResponse;
import com.selimhorri.app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = { "/api/users" })
@Slf4j
@RequiredArgsConstructor
public class UserResource {

	private final UserService userService;

	@GetMapping
	public ResponseEntity<DtoCollectionResponse<UserResponseDto>> findAll() {
		log.info("*** UserResponseDto List, controller; fetch all users *");
		return ResponseEntity.ok(new DtoCollectionResponse<>(this.userService.findAll()));
	}

	@GetMapping("/{userId}")
	public ResponseEntity<UserResponseDto> findById(
			@PathVariable("userId") @NotBlank(message = "Input must not be blank") final String userId) {
		log.info("*** UserResponseDto, resource; fetch user by id *");
		return ResponseEntity.ok(this.userService.findById(Integer.parseInt(userId.strip())));
	}

	@GetMapping("/username/{username}")
	public ResponseEntity<UserResponseDto> findByUsername(
			@PathVariable("username") @NotBlank(message = "Input must not be blank") final String username) {
		log.info("*** UserResponseDto, resource; fetch user by username *");
		return ResponseEntity.ok(this.userService.findByUsername(username));
	}

	@PostMapping
	public ResponseEntity<UserResponseDto> create(
			@RequestBody @NotNull(message = "Input must not be NULL") @Valid final UserCreateRequestDto userCreateRequestDto) {
		log.info("*** UserResponseDto, resource; create user *");
		UserResponseDto createdUser = this.userService.create(userCreateRequestDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
	}

	@PutMapping("/{userId}")
	public ResponseEntity<UserResponseDto> update(
			@PathVariable("userId") @NotBlank(message = "Input must not be blank") final String userId,
			@RequestBody @NotNull(message = "Input must not be NULL") @Valid final UserUpdateRequestDto userUpdateRequestDto) {
		log.info("*** UserResponseDto, resource; update user with userId {} *", userId);
		return ResponseEntity.ok(this.userService.update(Integer.parseInt(userId.strip()), userUpdateRequestDto));
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<Void> deleteById(
			@PathVariable("userId") @NotBlank(message = "Input must not be blank") final String userId) {
		log.info("*** Void, resource; delete user by id *");
		this.userService.deleteById(Integer.parseInt(userId.strip()));
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
package com.selimhorri.app.service.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.selimhorri.app.domain.User;
import com.selimhorri.app.domain.VerificationToken;
import com.selimhorri.app.dto.request.UserCreateRequestDto;
import com.selimhorri.app.dto.request.UserUpdateRequestDto;
import com.selimhorri.app.dto.response.UserResponseDto;
import com.selimhorri.app.exception.wrapper.UserObjectNotFoundException;
import com.selimhorri.app.helper.UserMapper;
import com.selimhorri.app.repository.UserRepository;
import com.selimhorri.app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	// ... (otros métodos: findAll, findById, findByUsername) ...
	@Override
	public List<UserResponseDto> findAll() {
		log.info("*** UserResponseDto List, service; fetch all users *");
		return this.userRepository.findAll()
				.stream()
				.map(UserMapper::toResponseDto)
				.collect(Collectors.toUnmodifiableList());
	}

	@Override
	public UserResponseDto findById(final Integer userId) {
		log.info("*** UserResponseDto, service; fetch user by id *");
		return this.userRepository.findById(userId)
				.map(UserMapper::toResponseDto)
				.orElseThrow(
						() -> new UserObjectNotFoundException(String.format("User with id: %d not found", userId)));
	}

	@Override
	public UserResponseDto findByUsername(final String username) {
		log.info("*** UserResponseDto, service; fetch user with username *");
		return this.userRepository.findByCredentialUsername(username)
				.map(UserMapper::toResponseDto)
				.orElseThrow(() -> new UserObjectNotFoundException(
						String.format("User with username: %s not found", username)));
	}

	@Override
	public UserResponseDto create(final UserCreateRequestDto userCreateRequestDto) {
		log.info("*** UserResponseDto, service; create new user with username: {} *",
				userCreateRequestDto.getUsername());

		// El UserMapper.toEntity ya se encarga de los timestamps del Credential
		final User user = UserMapper.toEntity(userCreateRequestDto);
		user.getCredential().setPassword(this.passwordEncoder.encode(user.getCredential().getPassword()));

		final String tokenValue = UUID.randomUUID().toString();
		final VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken(tokenValue);
		verificationToken.setExpireDate(LocalDate.now().plusDays(1));
		verificationToken.setCredential(user.getCredential());
		user.getCredential().setVerificationTokens(Set.of(verificationToken));

		// Obtenemos la hora actual UNA VEZ para consistencia
		final Instant now = Instant.now();

		// Establecemos timestamps para la entidad User
		user.setCreatedAt(now);
		user.setUpdatedAt(now);

		// ==================================================================
		// ===== INYECCIÓN MANUAL DE TIMESTAMPS PARA VERIFICATION TOKEN =====
		verificationToken.setCreatedAt(now);
		verificationToken.setUpdatedAt(now);
		// ==================================================================

		final User savedUser = this.userRepository.save(user);

		return UserMapper.toResponseDto(savedUser);
	}

	// ... (otros métodos: update, deleteById) ...
	@Override
	public UserResponseDto update(final Integer userId, final UserUpdateRequestDto userUpdateRequestDto) {
		log.info("*** UserResponseDto, service; update user with id: {} *", userId);

		final User existingUser = this.userRepository.findById(userId)
				.orElseThrow(
						() -> new UserObjectNotFoundException(String.format("User with id: %d not found", userId)));

		UserMapper.updateEntityFromDto(existingUser, userUpdateRequestDto);

		// Al actualizar el User, su `updatedAt` también debería actualizarse
		// Si la auditoría automática NO funciona, necesitarías esto:
		// existingUser.setUpdatedAt(Instant.now());
		// Pero como AbstractMappedEntity tiene @LastModifiedDate y @EnableJpaAuditing,
		// debería ser automático.
		// Si no lo es para H2, considera también la línea anterior.

		final User updatedUser = this.userRepository.save(existingUser);

		return UserMapper.toResponseDto(updatedUser);
	}

	@Override
	public void deleteById(final Integer userId) {
		log.info("*** Void, service; delete user by id *");
		if (!this.userRepository.existsById(userId)) {
			throw new UserObjectNotFoundException(String.format("User with id: %d not found", userId));
		}
		this.userRepository.deleteById(userId);
	}

}
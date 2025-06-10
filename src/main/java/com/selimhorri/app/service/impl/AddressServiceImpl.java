package com.selimhorri.app.service.impl;

import java.time.Instant; // IMPORTANTE
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.selimhorri.app.domain.Address;
import com.selimhorri.app.domain.User;
import com.selimhorri.app.dto.request.AddressCreateRequestDto;
import com.selimhorri.app.dto.request.AddressUpdateRequestDto;
import com.selimhorri.app.dto.response.AddressResponseDto;
import com.selimhorri.app.exception.wrapper.AddressNotFoundException;
import com.selimhorri.app.exception.wrapper.UserObjectNotFoundException;
import com.selimhorri.app.helper.AddressMapper;
import com.selimhorri.app.repository.AddressRepository;
import com.selimhorri.app.repository.UserRepository;
import com.selimhorri.app.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

	private final AddressRepository addressRepository;
	private final UserRepository userRepository;

	@Override
	public List<AddressResponseDto> findAll() {
		log.info("*** AddressResponseDto List, service; fetch all addresses *");
		return this.addressRepository.findAll()
				.stream()
				.map(AddressMapper::toResponseDto)
				.collect(Collectors.toUnmodifiableList());
	}

	@Override
	public AddressResponseDto findById(final Integer addressId) {
		log.info("*** AddressResponseDto, service; fetch address by id *");
		return this.addressRepository.findById(addressId)
				.map(AddressMapper::toResponseDto)
				.orElseThrow(() -> new AddressNotFoundException(
						String.format("Address with id: %d not found!", addressId)));
	}

	@Override
	public AddressResponseDto create(final AddressCreateRequestDto dto) {
		log.info("*** AddressResponseDto, service; create address for user id: {} *", dto.getUserId());

		final User user = this.userRepository.findById(dto.getUserId())
				.orElseThrow(() -> new UserObjectNotFoundException(
						String.format("Cannot create address, user with id: %d not found", dto.getUserId())));

		final Address address = new Address();
		address.setFullAddress(dto.getFullAddress());
		address.setPostalCode(dto.getPostalCode());
		address.setCity(dto.getCity());
		address.setUser(user);

		// Inyección manual de timestamps
		final Instant now = Instant.now();
		address.setCreatedAt(now);
		address.setUpdatedAt(now);

		return AddressMapper.toResponseDto(this.addressRepository.save(address));
	}

	@Override
	public AddressResponseDto update(final Integer addressId, final AddressUpdateRequestDto dto) {
		log.info("*** AddressResponseDto, service; update address with id: {} *", addressId);

		final Address existingAddress = this.addressRepository.findById(addressId)
				.orElseThrow(() -> new AddressNotFoundException(
						String.format("Address with id: %d not found!", addressId)));

		AddressMapper.updateEntityFromDto(existingAddress, dto);

		// Inyección manual de timestamp de actualización
		existingAddress.setUpdatedAt(Instant.now());

		return AddressMapper.toResponseDto(this.addressRepository.save(existingAddress));
	}

	@Override
	public void deleteById(final Integer addressId) {
		log.info("*** Void, service; delete address by id: {} *", addressId);
		if (!this.addressRepository.existsById(addressId)) {
			throw new AddressNotFoundException(String.format("Address with id: %d not found!", addressId));
		}
		this.addressRepository.deleteById(addressId);
	}
}
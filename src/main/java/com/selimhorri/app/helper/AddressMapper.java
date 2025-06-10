package com.selimhorri.app.helper;

import com.selimhorri.app.domain.Address;
import com.selimhorri.app.dto.request.AddressUpdateRequestDto;
import com.selimhorri.app.dto.response.AddressResponseDto;

public final class AddressMapper {

	private AddressMapper() {
	}

	/**
	 * Maps an Address entity to an AddressResponseDto.
	 * Reuses UserMapper to map the nested user object.
	 */
	public static AddressResponseDto toResponseDto(final Address address) {
		return AddressResponseDto.builder()
				.addressId(address.getAddressId())
				.fullAddress(address.getFullAddress())
				.postalCode(address.getPostalCode())
				.city(address.getCity())
				.user(UserMapper.toResponseDto(address.getUser())) // Reutilización del mapper de User
				.build();
	}

	/**
	 * Updates an existing Address entity from an AddressUpdateRequestDto.
	 * This method does not map the user, as the owner of an address should not
	 * change.
	 */
	public static void updateEntityFromDto(final Address address, final AddressUpdateRequestDto dto) {
		address.setFullAddress(dto.getFullAddress());
		address.setPostalCode(dto.getPostalCode());
		address.setCity(dto.getCity());
	}

}
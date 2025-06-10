package com.selimhorri.app.service;

import java.util.List;
import com.selimhorri.app.dto.request.AddressCreateRequestDto;
import com.selimhorri.app.dto.request.AddressUpdateRequestDto;
import com.selimhorri.app.dto.response.AddressResponseDto;

public interface AddressService {

	List<AddressResponseDto> findAll();

	AddressResponseDto findById(final Integer addressId);

	AddressResponseDto create(final AddressCreateRequestDto addressCreateRequestDto);

	AddressResponseDto update(final Integer addressId, final AddressUpdateRequestDto addressUpdateRequestDto);

	void deleteById(final Integer addressId);

}
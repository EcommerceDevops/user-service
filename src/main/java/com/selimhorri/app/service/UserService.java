package com.selimhorri.app.service;

import java.util.List;
import com.selimhorri.app.dto.request.UserCreateRequestDto;
import com.selimhorri.app.dto.request.UserUpdateRequestDto;
import com.selimhorri.app.dto.response.UserResponseDto;

public interface UserService {

	List<UserResponseDto> findAll();

	UserResponseDto findById(final Integer userId);

	UserResponseDto findByUsername(final String username);

	UserResponseDto create(final UserCreateRequestDto userCreateRequestDto);

	UserResponseDto update(final Integer userId, final UserUpdateRequestDto userUpdateRequestDto);

	void deleteById(final Integer userId);

}
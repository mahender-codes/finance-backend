package com.mahender.finance.service;

import java.util.List;
import org.springframework.http.ResponseEntity;
import com.mahender.finance.ResponseStructure;
import com.mahender.finance.dto.UserRequestDto;
import com.mahender.finance.dto.UserResponseDto;

public interface UserService {
	ResponseEntity<ResponseStructure<UserResponseDto>> createUser(UserRequestDto request);

	ResponseEntity<ResponseStructure<List<UserResponseDto>>> getAllUsers(Long requestedBy);

	ResponseEntity<ResponseStructure<UserResponseDto>> getUserById(Long id, Long requestedBy);

	ResponseEntity<ResponseStructure<UserResponseDto>> updateUser(Long id, UserRequestDto request, Long requestedBy);

	ResponseEntity<ResponseStructure<UserResponseDto>> changeStatus(Long userId, Long requestedBy);
}
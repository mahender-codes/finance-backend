package com.mahender.finance.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mahender.finance.ResponseStructure;
import com.mahender.finance.dto.UserRequestDto;
import com.mahender.finance.dto.UserResponseDto;
import com.mahender.finance.enums.Role;
import com.mahender.finance.exception.UnauthorizedException;
import com.mahender.finance.exception.UserAlreadyExistException;
import com.mahender.finance.exception.UserInactiveException;
import com.mahender.finance.exception.UserNotFoundException;
import com.mahender.finance.model.User;
import com.mahender.finance.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDto>> createUser(UserRequestDto request) {

		// Check if user already exists
		User existUser = userRepository.getByEmail(request.getEmail());
		if (existUser != null)
			throw new UserAlreadyExistException("User with the given email already exists.");

		// Create new user
		User user = new User();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setRole(request.getRole());
		user.setStatus(true);

		userRepository.save(user);

		return new ResponseEntity<>(
				new ResponseStructure<>(HttpStatus.CREATED.value(), "User created successfully.", mapToDto(user)),
				HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<List<UserResponseDto>>> getAllUsers(Long requestedBy) {

		// Validate user and check admin access
		User requestedUser = getValidatedUser(requestedBy);
		if (requestedUser.getRole() != Role.ADMIN)
			throw new UnauthorizedException("Access denied. Only admin can perform this action.");

		List<User> users = userRepository.findAll();
		List<UserResponseDto> dtos = new ArrayList<>();

		for (User user : users)
			dtos.add(mapToDto(user));

		return ResponseEntity.ok(new ResponseStructure<>(HttpStatus.OK.value(), "Users retrieved successfully.", dtos));
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDto>> getUserById(Long id, Long requestedBy) {

		// Validate user and check admin access
		User requestedUser = getValidatedUser(requestedBy);
		if (requestedUser.getRole() != Role.ADMIN)
			throw new UnauthorizedException("Access denied. Only admin can perform this action.");

		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User not found with the given ID " + id));

		return ResponseEntity
				.ok(new ResponseStructure<>(HttpStatus.OK.value(), "User retrieved successfully.", mapToDto(user)));
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDto>> updateUser(Long userId, UserRequestDto request,
			Long requestedBy) {

		// Validate user and check admin access
		User requestedUser = getValidatedUser(requestedBy);
		if (requestedUser.getRole() != Role.ADMIN)
			throw new UnauthorizedException("Access denied. Only admin can perform this action.");

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found with the given ID " + userId));

		// Update only given fields
		if (request.getName() != null)
			user.setName(request.getName());

		if (request.getEmail() != null)
			user.setEmail(request.getEmail());

		if (request.getRole() != null)
			user.setRole(request.getRole());

		userRepository.save(user);

		return ResponseEntity
				.ok(new ResponseStructure<>(HttpStatus.OK.value(), "User updated successfully.", mapToDto(user)));
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponseDto>> changeStatus(Long userId, Long requestedBy) {

		// Validate admin and check role
		User admin = getValidatedUser(requestedBy);
		if (admin.getRole() != Role.ADMIN)
			throw new UnauthorizedException("Access denied. Only admin can change user status.");

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found with the given ID " + userId));

		// Toggle status
		user.setStatus(!user.getStatus());

		userRepository.save(user);

		return ResponseEntity.ok(
				new ResponseStructure<>(HttpStatus.OK.value(), "User status updated successfully.", mapToDto(user)));
	}

	private User getValidatedUser(Long requestedBy) {
		User user = userRepository.findById(requestedBy)
				.orElseThrow(() -> new UserNotFoundException("User not found with the given ID " + requestedBy));

		if (!user.getStatus())
			throw new UserInactiveException("Your account is inactive. Please contact admin.");

		return user;
	}

	private UserResponseDto mapToDto(User user) {
		return new UserResponseDto(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.getStatus());
	}
}
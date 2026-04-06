package com.mahender.finance.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mahender.finance.ResponseStructure;
import com.mahender.finance.dto.UserRequestDto;
import com.mahender.finance.dto.UserResponseDto;
import com.mahender.finance.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
	@Autowired
	private UserService userService;

	@PostMapping()
	public ResponseEntity<ResponseStructure<UserResponseDto>> createUser(@RequestBody @Valid UserRequestDto request) {
		return userService.createUser(request);
	}

	@GetMapping()
	public ResponseEntity<ResponseStructure<List<UserResponseDto>>> getAllUsers(@RequestParam Long requestedBy) {
		return userService.getAllUsers(requestedBy);
	}

	@GetMapping("/{userId}")
	public ResponseEntity<ResponseStructure<UserResponseDto>> getUserById(@PathVariable Long userId,
			@RequestParam Long requestedBy) {
		return userService.getUserById(userId, requestedBy);
	}

	@PutMapping("/{userId}")
	public ResponseEntity<ResponseStructure<UserResponseDto>> updateUser(@PathVariable Long userId,
			@RequestBody UserRequestDto request, @RequestParam Long requestedBy) {
		return userService.updateUser(userId, request, requestedBy);
	}

	@PatchMapping("/status/{userId}")
	public ResponseEntity<ResponseStructure<UserResponseDto>> changeStatus(@PathVariable Long userId,
			@RequestParam Long requestedBy) {
		return userService.changeStatus(userId, requestedBy);
	}
}

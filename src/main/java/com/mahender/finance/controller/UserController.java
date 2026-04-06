package com.mahender.finance.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mahender.finance.ResponseStructure;
import com.mahender.finance.dto.UserRequestDto;
import com.mahender.finance.dto.UserResponseDto;
import com.mahender.finance.security.RoleChecker;
import com.mahender.finance.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleChecker roleChecker;

	@PostMapping("/registerAdmin")
	public ResponseEntity<ResponseStructure<UserResponseDto>> createNewAdminUser(
			@RequestBody @Valid UserRequestDto dto) {

		roleChecker.checkNewUserRole(dto);
		return userService.createUser(dto);
	}

	@PostMapping
	public ResponseEntity<ResponseStructure<UserResponseDto>> createUser(HttpServletRequest request,
			@RequestBody @Valid UserRequestDto dto) {

		roleChecker.checkAdmin(request);
		return userService.createUser(dto);
	}

	@GetMapping("/all")
	public ResponseEntity<ResponseStructure<List<UserResponseDto>>> getAllUsers(HttpServletRequest request) {

		roleChecker.checkAdmin(request);
		return userService.getAllUsers();
	}

	@GetMapping("/{userId}")
	public ResponseEntity<ResponseStructure<UserResponseDto>> getUserById(@PathVariable Long userId,
			HttpServletRequest request) {

		roleChecker.checkAdmin(request);
		return userService.getUserById(userId);
	}

	@PutMapping("/{userId}")
	public ResponseEntity<ResponseStructure<UserResponseDto>> updateUser(@PathVariable Long userId,
			@RequestBody UserRequestDto dto, HttpServletRequest request) {

		roleChecker.checkAdmin(request);
		return userService.updateUser(userId, dto);
	}

	@PatchMapping("/status/{userId}")
	public ResponseEntity<ResponseStructure<UserResponseDto>> changeStatus(@PathVariable Long userId,
			HttpServletRequest request) {

		roleChecker.checkAdmin(request);
		return userService.changeStatus(userId);
	}
}
package com.mahender.finance.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mahender.finance.dto.UserRequestDto;
import com.mahender.finance.enums.Role;
import com.mahender.finance.exception.UnauthorizedException;
import com.mahender.finance.exception.UserInactiveException;
import com.mahender.finance.model.User;
import com.mahender.finance.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class RoleChecker {

	@Autowired
	private UserRepository userRepository;

	public Role getRole(HttpServletRequest request) {
		String roleStr = request.getHeader("role");

		if (roleStr == null) {
			throw new UnauthorizedException("Role not provided");
		}

		try {
			return Role.valueOf(roleStr.toUpperCase());
		} catch (Exception e) {
			throw new UnauthorizedException("Invalid role");
		}
	}

	public Long getUserId(HttpServletRequest request) {
		String userId = request.getHeader("userId");

		if (userId == null || userId.isEmpty()) {
			throw new UnauthorizedException("User ID not provided");
		}

		try {
			return Long.parseLong(userId);
		} catch (Exception e) {
			throw new UnauthorizedException("Invalid user ID");
		}
	}

	public void validateUser(HttpServletRequest request) {
		Long userId = getUserId(request);

		User user = userRepository.findById(userId).orElseThrow(() -> new UnauthorizedException("User not found"));

		if (!user.getStatus()) {
			throw new UserInactiveException("Your account is Inactive,Please contact admin");
		}
	}

	public void checkAdmin(HttpServletRequest request) {
		validateUser(request);
		
		User user = userRepository
				.findById(getUserId(request)).orElseThrow(() -> new UnauthorizedException("User not found"));


		if (getRole(request) != Role.ADMIN || user.getRole() != getRole(request)) {
			throw new UnauthorizedException("Access denied: Admin privileges required.");
		}
	}

	public void checkNewUserRole(UserRequestDto dto) {
		if (dto.getRole() != Role.ADMIN) {
			throw new UnauthorizedException("Invalid role: Only ADMIN role can be assigned during registration.");
		}
	}

	public void checkAnalystOrAdmin(HttpServletRequest request) {
		validateUser(request);

		Role role = getRole(request);

		if (role != Role.ADMIN && role != Role.ANALYST) {
			throw new UnauthorizedException("Only ADMIN or ANALYST users allowed");
		}
	}
}
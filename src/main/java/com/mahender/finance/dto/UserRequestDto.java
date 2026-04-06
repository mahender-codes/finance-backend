package com.mahender.finance.dto;

import com.mahender.finance.enums.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

	@NotBlank(message = "Name is required.")
	@Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters.")
	private String name;

	@NotBlank(message = "Email is required.")
	@Email(message = "Invalid email format.")
	private String email;

	@NotNull(message = "Role is required")
	private Role role;
}

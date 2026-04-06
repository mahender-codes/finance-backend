package com.mahender.finance.dto;

import com.mahender.finance.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
	private long id;
	private String name;
	private String email;
	private Role role;
	private Boolean status;
}

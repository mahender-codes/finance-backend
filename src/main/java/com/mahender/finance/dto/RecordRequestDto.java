package com.mahender.finance.dto;

import java.time.LocalDate;

import com.mahender.finance.enums.RecordType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecordRequestDto {

	@NotNull(message = "Amount is required")
	@Positive(message = "Amount must be greater than zero")
	private Double amount;

	@NotNull(message = "Type is required")
	private RecordType type;

	@NotBlank(message = "Category cannot be empty")
	private String category;

	@NotNull(message = "Date is required")
	@PastOrPresent(message = "Date cannot be in the future")
	private LocalDate date;

	@Size(max = 255, message = "Notes cannot exceed 255 characters")
	private String notes;
}
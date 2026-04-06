package com.mahender.finance.dto;

import java.time.LocalDate;

import com.mahender.finance.enums.RecordType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecordResponseDto {
	private long id;
	private double amount;
	private RecordType type;
	private String category;
	private LocalDate date;
	private String notes;
	private Long createdBy;
}

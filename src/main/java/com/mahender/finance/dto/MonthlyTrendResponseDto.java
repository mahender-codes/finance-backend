package com.mahender.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyTrendResponseDto {
	private String month;
	private Double totalIncome;
	private Double totalExpenses;
}

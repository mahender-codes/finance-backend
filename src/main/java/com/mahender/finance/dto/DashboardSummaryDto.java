package com.mahender.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryDto {
	private String name;
	private Double totalIncome;
	private Double totalExpenses;
	private Double netBalance;
}

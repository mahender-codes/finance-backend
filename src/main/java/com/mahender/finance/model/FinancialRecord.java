package com.mahender.finance.model;

import java.time.LocalDate;

import com.mahender.finance.enums.RecordType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "financial_records")
public class FinancialRecord {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private Double amount;

	@Enumerated(EnumType.STRING)
	private RecordType type;

	private String category;

	private LocalDate date;

	private String notes;

	private Long createdBy;

	@Column(nullable = false)
	private boolean deleted = false;
}

package com.mahender.finance.dto;

import java.time.LocalDate;

import com.mahender.finance.enums.RecordType;

import jakarta.validation.constraints.*;
import lombok.*;

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
    @Size(max = 100, message = "Category cannot exceed 100 characters")
    private String category;

    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date cannot be in the future")
    private LocalDate date;

    @Size(max = 255, message = "Notes cannot exceed 255 characters")
    private String notes;
}
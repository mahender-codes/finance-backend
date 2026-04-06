package com.mahender.finance.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.mahender.finance.ResponseStructure;
import com.mahender.finance.dto.RecordRequestDto;
import com.mahender.finance.dto.RecordResponseDto;
import com.mahender.finance.enums.RecordType;

public interface RecordService {

	ResponseEntity<ResponseStructure<RecordResponseDto>> createRecord(RecordRequestDto request, Long userId);

	ResponseEntity<ResponseStructure<List<RecordResponseDto>>> getAllRecords(int page, int size);

	ResponseEntity<ResponseStructure<RecordResponseDto>> getRecordById(Long id);

	ResponseEntity<ResponseStructure<RecordResponseDto>> updateRecord(Long id, RecordRequestDto request);

	ResponseEntity<?> deleteRecord(Long id);

	ResponseEntity<ResponseStructure<List<RecordResponseDto>>> filterRecords(RecordType type, String category,
			LocalDate startDate, LocalDate endDate);
}
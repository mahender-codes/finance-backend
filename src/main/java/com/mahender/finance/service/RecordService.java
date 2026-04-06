package com.mahender.finance.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.mahender.finance.ResponseStructure;
import com.mahender.finance.dto.RecordRequestDto;
import com.mahender.finance.dto.RecordResponseDto;
import com.mahender.finance.enums.RecordType;

public interface RecordService {

	ResponseEntity<ResponseStructure<RecordResponseDto>> createRecord(RecordRequestDto request, Long requestedBy);

	ResponseEntity<ResponseStructure<List<RecordResponseDto>>> getAllRecords(Long requestedBy);

	ResponseEntity<ResponseStructure<RecordResponseDto>> getRecordById(Long id, Long requestedBy);

	ResponseEntity<ResponseStructure<RecordResponseDto>> updateRecord(Long id, RecordRequestDto request,
			Long requestedBy);

	ResponseEntity<?> deleteRecord(Long id, Long requestedBy);

	ResponseEntity<ResponseStructure<List<RecordResponseDto>>> filterRecords(Long requestedBy, RecordType type,
			String category, LocalDate startDate, LocalDate endDate);
}
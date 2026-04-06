package com.mahender.finance.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mahender.finance.ResponseStructure;
import com.mahender.finance.dto.RecordRequestDto;
import com.mahender.finance.dto.RecordResponseDto;
import com.mahender.finance.enums.RecordType;
import com.mahender.finance.service.RecordService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/records")
public class RecordController {

	@Autowired
	private RecordService recordService;

	@PostMapping()
	public ResponseEntity<ResponseStructure<RecordResponseDto>> createRecord(
			@RequestBody @Valid RecordRequestDto request, @RequestParam Long requestedBy) {
		return recordService.createRecord(request, requestedBy);
	}

	@GetMapping()
	public ResponseEntity<ResponseStructure<List<RecordResponseDto>>> getAllRecords(@RequestParam Long requestedBy) {
		return recordService.getAllRecords(requestedBy);
	}

	@GetMapping("/{recordId}")
	public ResponseEntity<ResponseStructure<RecordResponseDto>> getRecordById(@PathVariable Long recordId,
			@RequestParam Long requestedBy) {
		return recordService.getRecordById(recordId, requestedBy);
	}

	@PutMapping("/{recordId}")
	public ResponseEntity<ResponseStructure<RecordResponseDto>> updateRecord(@PathVariable Long recordId,
			@RequestBody RecordRequestDto request, @RequestParam Long requestedBy) {
		return recordService.updateRecord(recordId, request, requestedBy);
	}

	@DeleteMapping("/{recordId}")
	public ResponseEntity<?> deleteRecord(@PathVariable Long recordId, @RequestParam Long requestedBy) {
		return recordService.deleteRecord(recordId, requestedBy);
	}

	@GetMapping(("/filter"))
	public ResponseEntity<ResponseStructure<List<RecordResponseDto>>> filterRecords(@RequestParam Long requestedBy,
			@RequestParam(required = false) RecordType type, @RequestParam(required = false) String category,
			@RequestParam(required = false) LocalDate startDate, @RequestParam(required = false) LocalDate endDate) {
		return recordService.filterRecords(requestedBy, type, category, startDate, endDate);
	}
}
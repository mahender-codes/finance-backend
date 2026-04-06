package com.mahender.finance.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mahender.finance.ResponseStructure;
import com.mahender.finance.dto.RecordRequestDto;
import com.mahender.finance.dto.RecordResponseDto;
import com.mahender.finance.enums.RecordType;
import com.mahender.finance.security.RoleChecker;
import com.mahender.finance.service.RecordService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/records")
public class RecordController {

	@Autowired
	private RecordService recordService;

	@Autowired
	private RoleChecker roleChecker;

	@PostMapping
	public ResponseEntity<ResponseStructure<RecordResponseDto>> createRecord(HttpServletRequest request,
			@RequestBody @Valid RecordRequestDto dto) {

		roleChecker.checkAdmin(request);

		Long userId = roleChecker.getUserId(request);

		return recordService.createRecord(dto, userId);
	}

	@GetMapping
	public ResponseEntity<ResponseStructure<List<RecordResponseDto>>> getAllRecords(HttpServletRequest request,
			@RequestParam int page, @RequestParam int size) {

		roleChecker.checkAnalystOrAdmin(request);

		return recordService.getAllRecords(page, size);
	}

	@GetMapping("/{recordId}")
	public ResponseEntity<ResponseStructure<RecordResponseDto>> getRecordById(@PathVariable Long recordId,
			HttpServletRequest request) {

		roleChecker.checkAnalystOrAdmin(request);

		return recordService.getRecordById(recordId);
	}

	@PutMapping("/{recordId}")
	public ResponseEntity<ResponseStructure<RecordResponseDto>> updateRecord(@PathVariable Long recordId,
			@RequestBody RecordRequestDto dto, HttpServletRequest request) {

		roleChecker.checkAdmin(request);

		return recordService.updateRecord(recordId, dto);
	}

	@DeleteMapping("/{recordId}")
	public ResponseEntity<?> deleteRecord(@PathVariable Long recordId, HttpServletRequest request) {

		roleChecker.checkAdmin(request);

		return recordService.deleteRecord(recordId);
	}

	@GetMapping("/filter")
	public ResponseEntity<ResponseStructure<List<RecordResponseDto>>> filterRecords(HttpServletRequest request,
			@RequestParam(required = false) RecordType type, @RequestParam(required = false) String category,
			@RequestParam(required = false) LocalDate startDate, @RequestParam(required = false) LocalDate endDate) {

		roleChecker.checkAnalystOrAdmin(request);

		return recordService.filterRecords(type, category, startDate, endDate);
	}
}
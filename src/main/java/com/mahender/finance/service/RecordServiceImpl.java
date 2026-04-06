package com.mahender.finance.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mahender.finance.ResponseStructure;
import com.mahender.finance.dto.RecordRequestDto;
import com.mahender.finance.dto.RecordResponseDto;
import com.mahender.finance.enums.RecordType;
import com.mahender.finance.enums.Role;
import com.mahender.finance.exception.RecordNotFoundException;
import com.mahender.finance.exception.UnauthorizedException;
import com.mahender.finance.exception.UserInactiveException;
import com.mahender.finance.exception.UserNotFoundException;
import com.mahender.finance.model.FinancialRecord;
import com.mahender.finance.model.User;
import com.mahender.finance.repository.RecordRepository;
import com.mahender.finance.repository.UserRepository;

@Service
public class RecordServiceImpl implements RecordService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RecordRepository recordRepository;

	@Override
	public ResponseEntity<ResponseStructure<RecordResponseDto>> createRecord(RecordRequestDto request,
			Long requestedBy) {

		// Fetch user and validate existence
		User requestedUser = getValidatedUser(requestedBy);

		// Only restrict viewers from creating records
		if (requestedUser.getRole() == Role.VIEWER)
			throw new UnauthorizedException("Access denied. You're not allowed to perform this action.");

		// Create new financial record
		FinancialRecord record = new FinancialRecord();
		record.setAmount(request.getAmount());
		record.setCategory(request.getCategory());
		record.setDate(request.getDate());
		record.setNotes(request.getNotes());
		record.setType(request.getType());
		record.setCreatedBy(requestedBy);

		recordRepository.save(record);

		// Map entity to DTO
		RecordResponseDto dto = mapToDto(record);

		return new ResponseEntity<>(
				new ResponseStructure<>(HttpStatus.CREATED.value(), "Record created successfully.", dto),
				HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<List<RecordResponseDto>>> getAllRecords(Long requestedBy) {
		// Fetch user and validate existence
		User requestedUser = getValidatedUser(requestedBy);

		// Only restrict viewers
		if (requestedUser.getRole() == Role.VIEWER)
			throw new UnauthorizedException("Access denied. You're not allowed to perform this action.");

		// Fetch only non-deleted records
		List<FinancialRecord> records = recordRepository.findByDeletedFalse();
		List<RecordResponseDto> responseDtos = new ArrayList<>();

		// Convert entities to DTOs
		for (FinancialRecord record : records) {
			responseDtos.add(mapToDto(record));
		}

		return ResponseEntity.ok(
				new ResponseStructure<>(HttpStatus.OK.value(), "All records retrieved successfully.", responseDtos));
	}

	@Override
	public ResponseEntity<ResponseStructure<RecordResponseDto>> getRecordById(Long id, Long requestedBy) {
		// Fetch user and validate existence
		User requestedUser = getValidatedUser(requestedBy);

		// Only restrict viewers
		if (requestedUser.getRole() == Role.VIEWER)
			throw new UnauthorizedException("Access denied. You're not allowed to perform this action.");

		// Fetch only non-deleted record by id
		FinancialRecord record = recordRepository.findByIdAndDeletedFalse(id)
				.orElseThrow(() -> new RecordNotFoundException("Financial record not found with the given ID " + id));

		RecordResponseDto responseDto = mapToDto(record);

		return ResponseEntity
				.ok(new ResponseStructure<>(HttpStatus.OK.value(), "Record retrieved successfully.", responseDto));
	}

	@Override
	public ResponseEntity<ResponseStructure<RecordResponseDto>> updateRecord(Long id, RecordRequestDto request,
			Long requestedBy) {
		User requestedUser = getValidatedUser(requestedBy);

		if (requestedUser.getRole() != Role.ADMIN)
			throw new UnauthorizedException("Access denied. Only admin can perform this action.");

		// Fetch only non-deleted record by id
		FinancialRecord record = recordRepository.findByIdAndDeletedFalse(id)
				.orElseThrow(() -> new RecordNotFoundException("Financial record not found with the given ID " + id));

		// Update only given fields
		if (request.getAmount() != null)
			record.setAmount(request.getAmount());

		if (request.getCategory() != null)
			record.setCategory(request.getCategory());

		if (request.getDate() != null)
			record.setDate(request.getDate());

		if (request.getNotes() != null)
			record.setNotes(request.getNotes());

		if (request.getType() != null)
			record.setType(request.getType());

		recordRepository.save(record);

		RecordResponseDto dto = mapToDto(record);

		return ResponseEntity.ok(new ResponseStructure<>(HttpStatus.OK.value(), "Record updated successfully.", dto));
	}

	@Override
	public ResponseEntity<ResponseStructure<List<RecordResponseDto>>> filterRecords(Long requestedBy, RecordType type,
			String category, LocalDate startDate, LocalDate endDate) {
		// Fetch user and validate existence
		User requestedUser = getValidatedUser(requestedBy);

		// Only restrict viewers
		if (requestedUser.getRole() == Role.VIEWER)
			throw new UnauthorizedException("Access denied. You're not allowed to perform this action.");

		// Fetch only non-deleted records
		List<FinancialRecord> records = recordRepository.findByDeletedFalse();
		List<RecordResponseDto> dtos = new ArrayList<>();

		for (FinancialRecord record : records) {

			// Skip if type not matched
			if (type != null && record.getType() != type)
				continue;

			// Skip if category not match
			if (category != null) {
				if (record.getCategory() == null || !category.equalsIgnoreCase(record.getCategory().trim()))
					continue;
			}

			// Skip if date not in the range
			if (startDate != null && endDate != null) {
				if (record.getDate() == null || record.getDate().isBefore(startDate)
						|| record.getDate().isAfter(endDate))
					continue;
			}

			// Add valid record
			dtos.add(mapToDto(record));
		}

		if (dtos.isEmpty()) {
			return ResponseEntity.ok(
					new ResponseStructure<>(HttpStatus.OK.value(), "No records found matching given filters", dtos));
		}

		return ResponseEntity
				.ok(new ResponseStructure<>(HttpStatus.OK.value(), "Records retrieved successfully", dtos));
	}

	@Override
	public ResponseEntity<?> deleteRecord(Long id, Long requestedBy) {
		User requestedUser = getValidatedUser(requestedBy);

		if (requestedUser.getRole() != Role.ADMIN)
			throw new UnauthorizedException("Access denied. Only admin can perform this action.");

		// Fetch only non-deleted record by id
		FinancialRecord record = recordRepository.findByIdAndDeletedFalse(id)
				.orElseThrow(() -> new RecordNotFoundException("Financial record not found with the given ID " + id));

		// Soft delete — mark as deleted instead of removing from DB
		record.setDeleted(true);
		recordRepository.save(record);

		return ResponseEntity.ok(new ResponseStructure<>(HttpStatus.OK.value(), "Record deleted successfully.", null));
	}

	private User getValidatedUser(Long requestedBy) {
		User user = userRepository.findById(requestedBy)
				.orElseThrow(() -> new UserNotFoundException("User not found with the given ID " + requestedBy));

		if (!user.getStatus())
			throw new UserInactiveException("Your account is inactive. Please contact admin.");

		return user;
	}

	private RecordResponseDto mapToDto(FinancialRecord record) {
		return new RecordResponseDto(record.getId(), record.getAmount() != null ? record.getAmount() : 0.0,
				record.getType(), record.getCategory() != null ? record.getCategory().trim() : "Uncategorized",
				record.getDate() != null ? record.getDate() : LocalDate.now(),
				record.getNotes() != null ? record.getNotes().trim() : "", record.getCreatedBy());
	}
}
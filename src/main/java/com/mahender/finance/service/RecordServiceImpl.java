package com.mahender.finance.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mahender.finance.ResponseStructure;
import com.mahender.finance.dto.RecordRequestDto;
import com.mahender.finance.dto.RecordResponseDto;
import com.mahender.finance.enums.RecordType;
import com.mahender.finance.exception.RecordNotFoundException;
import com.mahender.finance.model.FinancialRecord;
import com.mahender.finance.repository.RecordRepository;

@Service
public class RecordServiceImpl implements RecordService {

	@Autowired
	private RecordRepository recordRepository;

	@Override
	public ResponseEntity<ResponseStructure<RecordResponseDto>> createRecord(RecordRequestDto request, Long userId) {

		FinancialRecord record = new FinancialRecord();

		record.setAmount(request.getAmount());
		record.setCategory(request.getCategory());
		record.setDate(request.getDate());
		record.setNotes(request.getNotes());
		record.setType(request.getType());
		record.setCreatedBy(userId);
		// store id who is created
		recordRepository.save(record);

		return new ResponseEntity<>(
				new ResponseStructure<>(HttpStatus.CREATED.value(), "Record created successfully.", mapToDto(record)),
				HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<List<RecordResponseDto>>> getAllRecords(int page, int size) {

		Page<FinancialRecord> records = recordRepository.findByDeletedFalse(PageRequest.of(page, size));

		List<RecordResponseDto> dtos = new ArrayList<>();

		for (FinancialRecord record : records.getContent()) {
			dtos.add(mapToDto(record));
		}

		return ResponseEntity
				.ok(new ResponseStructure<>(HttpStatus.OK.value(), "All records retrieved successfully.", dtos));
	}

	@Override
	public ResponseEntity<ResponseStructure<RecordResponseDto>> getRecordById(Long id) {

		FinancialRecord record = recordRepository.findByIdAndDeletedFalse(id)
				.orElseThrow(() -> new RecordNotFoundException("Financial record not found with ID " + id));

		return ResponseEntity
				.ok(new ResponseStructure<>(HttpStatus.OK.value(), "Record retrieved successfully.", mapToDto(record)));
	}

	@Override
	public ResponseEntity<ResponseStructure<RecordResponseDto>> updateRecord(Long id, RecordRequestDto request) {

		FinancialRecord record = recordRepository.findByIdAndDeletedFalse(id)
				.orElseThrow(() -> new RecordNotFoundException("Financial record not found with ID " + id));

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

		return ResponseEntity
				.ok(new ResponseStructure<>(HttpStatus.OK.value(), "Record updated successfully.", mapToDto(record)));
	}

	@Override
	public ResponseEntity<?> deleteRecord(Long id) {

		FinancialRecord record = recordRepository.findByIdAndDeletedFalse(id)
				.orElseThrow(() -> new RecordNotFoundException("Financial record not found with ID " + id));

		record.setDeleted(true);
		recordRepository.save(record);

		return ResponseEntity.ok(new ResponseStructure<>(HttpStatus.OK.value(), "Record deleted successfully.", null));
	}

	@Override
	public ResponseEntity<ResponseStructure<List<RecordResponseDto>>> filterRecords(RecordType type, String category,
			LocalDate startDate, LocalDate endDate) {

		List<FinancialRecord> records = recordRepository.findByDeletedFalse();
		List<RecordResponseDto> dtos = new ArrayList<>();

		for (FinancialRecord record : records) {

			if (type != null && record.getType() != type)
				continue;

			if (category != null) {
				if (record.getCategory() == null || !category.equalsIgnoreCase(record.getCategory().trim()))
					continue;
			}

			if (startDate != null && endDate != null) {
				if (record.getDate() == null || record.getDate().isBefore(startDate)
						|| record.getDate().isAfter(endDate))
					continue;
			}

			dtos.add(mapToDto(record));
		}

		return ResponseEntity
				.ok(new ResponseStructure<>(HttpStatus.OK.value(), "Records retrieved successfully", dtos));
	}

	private RecordResponseDto mapToDto(FinancialRecord record) {
		return new RecordResponseDto(record.getId(), record.getAmount() != null ? record.getAmount() : 0.0,
				record.getType(), record.getCategory() != null ? record.getCategory().trim() : "Uncategorized",
				record.getDate() != null ? record.getDate() : LocalDate.now(),
				record.getNotes() != null ? record.getNotes().trim() : "", record.getCreatedBy());
	}
}
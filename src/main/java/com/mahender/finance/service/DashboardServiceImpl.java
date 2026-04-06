package com.mahender.finance.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mahender.finance.ResponseStructure;
import com.mahender.finance.dto.DashboardSummaryDto;
import com.mahender.finance.dto.MonthlyTrendResponseDto;
import com.mahender.finance.dto.RecordResponseDto;
import com.mahender.finance.enums.RecordType;
import com.mahender.finance.model.FinancialRecord;
import com.mahender.finance.model.User;
import com.mahender.finance.repository.RecordRepository;
import com.mahender.finance.repository.UserRepository;

@Service
public class DashboardServiceImpl implements DashboardService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RecordRepository recordRepository;

	@Override
	public ResponseEntity<ResponseStructure<Map<Long, DashboardSummaryDto>>> getSummary() {

		List<FinancialRecord> records = recordRepository.findByDeletedFalse();
		Map<Long, DashboardSummaryDto> map = new HashMap<>();

		if (records.isEmpty()) {
			return ResponseEntity
					.ok(new ResponseStructure<>(HttpStatus.OK.value(), "No financial records available", map));
		}

		for (FinancialRecord record : records) {

			Optional<User> optionalUser = userRepository.findById(record.getCreatedBy());
			if (optionalUser.isEmpty())
				continue;

			User user = optionalUser.get();

			DashboardSummaryDto dto = map.get(record.getCreatedBy());

			if (dto == null) {
				dto = new DashboardSummaryDto();
				dto.setName(user.getName());
				dto.setTotalIncome(0.0);
				dto.setTotalExpenses(0.0);
				map.put(record.getCreatedBy(), dto);
			}

			if (record.getType() == RecordType.INCOME)
				dto.setTotalIncome(dto.getTotalIncome() + record.getAmount());
			else
				dto.setTotalExpenses(dto.getTotalExpenses() + record.getAmount());

			dto.setNetBalance(dto.getTotalIncome() - dto.getTotalExpenses());
		}

		return ResponseEntity
				.ok(new ResponseStructure<>(HttpStatus.OK.value(), "Dashboard summary retrieved successfully.", map));
	}

	@Override
	public ResponseEntity<ResponseStructure<Map<String, Double>>> getCategoryWise(Long userId) {

		List<FinancialRecord> records = recordRepository.findByCreatedByAndDeletedFalse(userId);
		Map<String, Double> map = new HashMap<>();

		if (records.isEmpty()) {
			return ResponseEntity
					.ok(new ResponseStructure<>(HttpStatus.OK.value(), "No financial records available", map));
		}

		for (FinancialRecord record : records) {

			String category = (record.getCategory() != null && !record.getCategory().trim().isEmpty())
					? record.getCategory().trim().toLowerCase()
					: "unknown";

			map.put(category, map.getOrDefault(category, 0.0) + record.getAmount());
		}

		return ResponseEntity.ok(
				new ResponseStructure<>(HttpStatus.OK.value(), "Category-wise summary retrieved successfully.", map));
	}

	@Override
	public ResponseEntity<ResponseStructure<List<RecordResponseDto>>> getRecentRecords() {

		List<FinancialRecord> records = recordRepository.findByDeletedFalse();

		if (records.isEmpty()) {
			return ResponseEntity.ok(new ResponseStructure<>(HttpStatus.OK.value(), "No financial records available",
					new ArrayList<>()));
		}

		List<RecordResponseDto> recentRecords = records.stream().filter(r -> r.getDate() != null)
				.sorted(Comparator.comparing(FinancialRecord::getDate).reversed()).limit(5).map(this::mapToDto)
				.toList();

		return ResponseEntity.ok(new ResponseStructure<>(HttpStatus.OK.value(),
				"Recent records retrieved successfully.", recentRecords));
	}

	@Override
	public ResponseEntity<ResponseStructure<List<MonthlyTrendResponseDto>>> getMonthlyData() {

		List<FinancialRecord> records = recordRepository.findByDeletedFalse();
		Map<String, MonthlyTrendResponseDto> map = new HashMap<>();

		for (FinancialRecord record : records) {

			LocalDate date = record.getDate();
			if (date == null)
				continue;

			String month = date.format(DateTimeFormatter.ofPattern("yyyy-MM"));

			MonthlyTrendResponseDto dto = map.get(month);

			if (dto == null) {
				dto = new MonthlyTrendResponseDto();
				dto.setMonth(month);
				dto.setTotalIncome(0.0);
				dto.setTotalExpenses(0.0);
				map.put(month, dto);
			}

			if (record.getType() == RecordType.INCOME)
				dto.setTotalIncome(dto.getTotalIncome() + record.getAmount());
			else
				dto.setTotalExpenses(dto.getTotalExpenses() + record.getAmount());
		}

		List<MonthlyTrendResponseDto> responseDtos = new ArrayList<>(map.values());
		responseDtos.sort(Comparator.comparing(MonthlyTrendResponseDto::getMonth));

		return ResponseEntity.ok(new ResponseStructure<>(HttpStatus.OK.value(),
				"Monthly records retrieved successfully.", responseDtos));
	}

	private RecordResponseDto mapToDto(FinancialRecord record) {
		return new RecordResponseDto(record.getId(), record.getAmount() != null ? record.getAmount() : 0.0,
				record.getType(), record.getCategory() != null ? record.getCategory().trim() : "Uncategorized",
				record.getDate() != null ? record.getDate() : LocalDate.now(),
				record.getNotes() != null ? record.getNotes().trim() : "", record.getCreatedBy());
	}
}
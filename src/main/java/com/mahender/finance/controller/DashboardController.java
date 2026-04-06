package com.mahender.finance.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mahender.finance.ResponseStructure;
import com.mahender.finance.dto.DashboardSummaryDto;
import com.mahender.finance.dto.MonthlyTrendResponseDto;
import com.mahender.finance.dto.RecordResponseDto;
import com.mahender.finance.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

	@Autowired
	private DashboardService dashboardService;

	@GetMapping("/summary")
	public ResponseEntity<ResponseStructure<Map<Long, DashboardSummaryDto>>> getSummary(
			@RequestParam Long requestedBy) {
		return dashboardService.getSummary(requestedBy);
	}

	@GetMapping("/category")
	public ResponseEntity<ResponseStructure<Map<String, Double>>> getCategorySummary(@RequestParam Long userId,
			@RequestParam Long requestedBy) {
		return dashboardService.getCategoryWise(userId, requestedBy);
	}

	@GetMapping("/monthly")
	public ResponseEntity<ResponseStructure<List<MonthlyTrendResponseDto>>> getMonthlySummary(
			@RequestParam Long requestedBy) {
		return dashboardService.getMonthlyData(requestedBy);
	}

	@GetMapping("/recent")
	public ResponseEntity<ResponseStructure<List<RecordResponseDto>>> getRecentRecords(@RequestParam Long requestedBy) {
		return dashboardService.getRecentRecords(requestedBy);
	}
}
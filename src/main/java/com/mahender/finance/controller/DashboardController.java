package com.mahender.finance.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mahender.finance.ResponseStructure;
import com.mahender.finance.dto.DashboardSummaryDto;
import com.mahender.finance.dto.MonthlyTrendResponseDto;
import com.mahender.finance.dto.RecordResponseDto;
import com.mahender.finance.security.RoleChecker;
import com.mahender.finance.service.DashboardService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

	@Autowired
	private DashboardService dashboardService;

	@Autowired
	private RoleChecker roleChecker;

	@GetMapping("/summary")
	public ResponseEntity<ResponseStructure<Map<Long, DashboardSummaryDto>>> getSummary(HttpServletRequest request) {

		roleChecker.validateUser(request);
		return dashboardService.getSummary();
	}

	@GetMapping("/category")
	public ResponseEntity<ResponseStructure<Map<String, Double>>> getCategorySummary(@RequestParam Long userId,
			HttpServletRequest request) {

		roleChecker.checkAnalystOrAdmin(request);
		return dashboardService.getCategoryWise(userId);
	}

	@GetMapping("/monthly")
	public ResponseEntity<ResponseStructure<List<MonthlyTrendResponseDto>>> getMonthlySummary(
			HttpServletRequest request) {

		roleChecker.checkAnalystOrAdmin(request);
		return dashboardService.getMonthlyData();
	}

	@GetMapping("/recent")
	public ResponseEntity<ResponseStructure<List<RecordResponseDto>>> getRecentRecords(HttpServletRequest request) {

		roleChecker.checkAnalystOrAdmin(request);
		return dashboardService.getRecentRecords();
	}
}
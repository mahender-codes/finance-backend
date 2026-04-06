package com.mahender.finance.service;

import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import com.mahender.finance.ResponseStructure;
import com.mahender.finance.dto.DashboardSummaryDto;
import com.mahender.finance.dto.MonthlyTrendResponseDto;
import com.mahender.finance.dto.RecordResponseDto;

public interface DashboardService {

	ResponseEntity<ResponseStructure<Map<Long, DashboardSummaryDto>>> getSummary();

	ResponseEntity<ResponseStructure<Map<String, Double>>> getCategoryWise(Long userId);

	ResponseEntity<ResponseStructure<List<RecordResponseDto>>> getRecentRecords();

	ResponseEntity<ResponseStructure<List<MonthlyTrendResponseDto>>> getMonthlyData();
}
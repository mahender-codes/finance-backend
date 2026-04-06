package com.mahender.finance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mahender.finance.model.FinancialRecord;

@Repository
public interface RecordRepository extends JpaRepository<FinancialRecord, Long> {
	List<FinancialRecord> findByCreatedByAndDeletedFalse(Long createdBy);

	List<FinancialRecord> findByDeletedFalse();

	Optional<FinancialRecord> findByIdAndDeletedFalse(Long id);

}
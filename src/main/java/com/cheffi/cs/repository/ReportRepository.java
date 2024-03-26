package com.cheffi.cs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cheffi.cs.constant.ReportReason;
import com.cheffi.cs.domain.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {

	@Query("select (count(r) > 0) "
		+ "from Report r "
		+ "where r.reporter.id = :reporter "
		+ "and r.target.id = :target "
		+ "and r.reason = :reason")
	boolean existsByReporterAndTargetAndReason(@Param("reporter") Long reporter, @Param("target") Long target,
		@Param("reason") ReportReason reason);
}

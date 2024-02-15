package com.cheffi.cs.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.avatar.domain.Avatar;
import com.cheffi.avatar.service.AvatarService;
import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.BusinessException;
import com.cheffi.cs.constant.ReportReason;
import com.cheffi.cs.domain.Report;
import com.cheffi.cs.dto.response.GetReportReasonResponse;
import com.cheffi.cs.dto.reqeust.PostReportRequest;
import com.cheffi.cs.repository.ReportRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ReportService {

	private final AvatarService avatarService;
	private final ReportRepository reportRepository;

	@Transactional
	public Long report(Long reporterId, PostReportRequest request) {
		if (reportRepository.existsByReporterAndTargetAndReason(reporterId, request.id(), request.reason())) {
			throw new BusinessException(ErrorCode.ALREADY_REPORTED);
		}
		Avatar reporter = avatarService.getById(reporterId);
		Avatar target = avatarService.getById(request.id());
		return reportRepository.save(new Report(reporter, target, request.reason())).getId();
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<GetReportReasonResponse> getReportReasons() {
		return Arrays.stream(ReportReason.values()).map(GetReportReasonResponse::new).toList();
	}
}

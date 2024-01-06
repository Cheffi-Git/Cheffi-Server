package com.cheffi.cs.domain;

import com.cheffi.avatar.domain.Avatar;
import com.cheffi.common.domain.BaseTimeEntity;
import com.cheffi.cs.constant.ReportReason;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Report extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reporter_id")
	private Avatar reporter;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "target_id")
	private Avatar target;

	@NotNull
	@Enumerated(EnumType.STRING)
	private ReportReason reason;

	public Report(Avatar reporter, Avatar target, ReportReason reason) {
		this.reporter = reporter;
		this.target = target;
		this.reason = reason;
	}

}

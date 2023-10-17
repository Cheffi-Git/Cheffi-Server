package com.cheffi.review.domain;

import java.math.BigDecimal;

import com.cheffi.common.constant.DetailedAddress;
import com.cheffi.common.domain.BaseTimeEntity;
import com.cheffi.review.constant.RestaurantStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Restaurant extends BaseTimeEntity implements RestaurantInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String name;

	@NotNull
	private String nameForQuery;

	private String category;

	@Column(precision = 15, scale = 9)
	private BigDecimal y;

	@Column(precision = 15, scale = 9)
	private BigDecimal x;

	@Enumerated(EnumType.STRING)
	private RestaurantStatus status;

	private int reviewCnt;

	@Embedded
	private DetailedAddress detailedAddress;

	public Restaurant(String name, DetailedAddress detailedAddress, RestaurantStatus status) {
		String trimmedName = name.trim().replaceAll("\\s+", " ");
		this.name = trimmedName;
		this.nameForQuery = trimmedName.replace(" ", "");
		this.status = status;
		this.detailedAddress = detailedAddress;
		this.reviewCnt = 0;
	}

	public Restaurant(String name, DetailedAddress detailedAddress, String category, BigDecimal y, BigDecimal x,
		RestaurantStatus status) {
		String trimmedName = name.trim().replaceAll("\\s+", " ");
		this.name = trimmedName;
		this.nameForQuery = trimmedName.replace(" ", "");
		this.category = category;
		this.y = y;
		this.x = x;
		this.status = status;
		this.detailedAddress = detailedAddress;
		this.reviewCnt = 0;
	}

	@Override
	public boolean isRegistered() {
		return true;
	}
}

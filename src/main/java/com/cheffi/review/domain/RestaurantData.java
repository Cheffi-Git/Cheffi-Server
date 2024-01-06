package com.cheffi.review.domain;

import java.math.BigDecimal;

import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.BusinessException;
import com.cheffi.common.constant.DetailedAddress;
import com.cheffi.review.constant.RestaurantStatus;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RestaurantData implements RestaurantInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	private String name;
	@NotNull
	private String nameForQuery;
	@NotNull
	private String manageNumber;
	@NotNull
	private String category;
	private boolean registered;
	@Column(precision = 15, scale = 9)
	private BigDecimal y;
	@Column(precision = 15, scale = 9)
	private BigDecimal x;
	@NotNull
	@Enumerated(EnumType.STRING)
	private RestaurantStatus status;
	@Embedded
	private DetailedAddress detailedAddress;
	@Nullable
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id")
	private Restaurant restaurant;

	public Restaurant toRestaurant() {
		if (registered)
			throw new BusinessException(ErrorCode.RESTAURANT_ALREADY_REGISTERED);
		registered = true;
		return new Restaurant(name, detailedAddress, category, y, x, status);
	}
}

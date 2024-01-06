package com.cheffi.review.domain;

import com.cheffi.avatar.domain.Avatar;
import com.cheffi.common.domain.BaseTimeEntity;
import com.cheffi.review.constant.RatingType;

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
public class Rating extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Enumerated(EnumType.STRING)
	private RatingType ratingType;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "review_id")
	private Review review;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "avatar_id")
	private Avatar avatar;

	private Rating(Avatar avatar, Review review, RatingType ratingType) {
		this.ratingType = ratingType;
		this.review = review;
		this.avatar = avatar;
	}

	public static Rating of(Avatar avatar, Review review, RatingType ratingType) {
		review.addRatingCount(ratingType);
		return new Rating(avatar, review, ratingType);
	}

	public void changeType(RatingType type) {
		if(this.ratingType.equals(type))
			return;
		this.review.changeRatingCount(this.ratingType, type);
		this.ratingType = type;
	}
}

package com.cheffi.review.domain;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cheffi.avatar.domain.Avatar;
import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.BusinessException;
import com.cheffi.common.domain.BaseTimeEntity;
import com.cheffi.review.constant.RatingType;
import com.cheffi.review.constant.ReviewStatus;
import com.cheffi.review.dto.request.UpdateReviewRequest;
import com.cheffi.tag.constant.TagType;
import com.cheffi.tag.domain.Tag;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Review extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String title;

	@NotNull
	@Column(length = 1000)
	private String text;
	private int goodRatingCnt;
	private int averageRatingCnt;
	private int badRatingCnt;
	private LocalDateTime timeToLock;
	private int viewCnt;

	@NotNull
	@Enumerated(EnumType.STRING)
	private ReviewStatus status;
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id")
	private Restaurant restaurant;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "writer_id")
	private Avatar writer;

	@OneToMany(mappedBy = "review")
	private List<Rating> ratings = new ArrayList<>();

	@OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ReviewPhoto> photos = new ArrayList<>();

	@OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Menu> menus = new ArrayList<>();

	@OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ReviewTag> reviewTags = new ArrayList<>();

	@OneToMany(mappedBy = "review")
	private List<ViewHistory> viewHistories = new ArrayList<>();

	Review(String title, String text, int lockAfterHours, Restaurant restaurant, Avatar writer) {
		this.title = title;
		this.text = text;
		this.timeToLock = LocalDateTime.now().plusHours(lockAfterHours);
		this.restaurant = restaurant;
		this.writer = writer;
		this.goodRatingCnt = 0;
		this.averageRatingCnt = 0;
		this.badRatingCnt = 0;
		this.viewCnt = 0;
		this.status = ReviewStatus.ACTIVE;
	}

	public static Review of(ReviewCreateRequest request, Restaurant restaurant, Avatar writer) {
		return new Review(request.title(), request.text(), request.lockAfterHours(), restaurant, writer);
	}

	public void addPhotos(List<ReviewPhoto> photos) {
		this.photos.addAll(photos);
	}

	public void addMenus(List<Menu> menus) {
		if (this.menus.size() + menus.size() > 5)
			throw new BusinessException(ErrorCode.TOO_MANY_MENUS);
		this.menus.addAll(menus);
	}

	public void clearMenus() {
		this.menus.clear();
	}

	public void clearPhotos() {
		this.photos.clear();
	}

	public void addTags(List<Tag> tagsToAdd) {
		List<Tag> tagList = reviewTags.stream().map(ReviewTag::getTag).toList();
		tagsToAdd.stream()
			.filter(t -> !tagList.contains(t))
			.map(t -> ReviewTag.mapTagToReview(this, t))
			.forEach(reviewTags::add);
	}

	public void removeTags(List<Tag> tagsToRemove) {
		reviewTags.removeIf(rt -> tagsToRemove.contains(rt.getTag()));
	}

	public List<Tag> getTags(TagType type) {
		return this.reviewTags.stream()
			.map(ReviewTag::getTag)
			.filter(tag -> tag.hasType(type))
			.toList();
	}

	public void updateFromRequest(UpdateReviewRequest request) {
		this.title = request.getTitle();
		this.text = request.getText();
	}

	public boolean isLocked() {
		return LocalDateTime.now().isAfter(timeToLock);
	}

	public boolean isActive() {
		return ReviewStatus.ACTIVE.equals(this.status);
	}

	public Long getTimeLeftToLock() {
		return Duration.between(LocalDateTime.now(), getTimeToLock()).toMillis();
	}

	public Map<RatingType, Integer> getRatingInfoMap() {
		return Map.of(RatingType.GOOD, goodRatingCnt, RatingType.AVERAGE, averageRatingCnt, RatingType.BAD,
			badRatingCnt);
	}

	public void read() {
		this.viewCnt += 1;
	}

	private void updateRatingCnt(RatingType type, int value) {
		if (RatingType.GOOD.equals(type))
			this.goodRatingCnt += value;
		else if (RatingType.AVERAGE.equals(type))
			this.averageRatingCnt += value;
		else if (RatingType.BAD.equals(type))
			this.badRatingCnt += value;
	}

	void addRatingCount(RatingType type) {
		updateRatingCnt(type, 1);
	}

	void removeRatingCount(RatingType type) {
		updateRatingCnt(type, -1);
	}

	void changeRatingCount(RatingType before, RatingType after) {
		removeRatingCount(before);
		addRatingCount(after);
	}

	public void delete() {
		this.status = ReviewStatus.DELETED;
	}
}

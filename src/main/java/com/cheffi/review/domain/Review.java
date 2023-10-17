package com.cheffi.review.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.cheffi.avatar.domain.Avatar;
import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.BusinessException;
import com.cheffi.common.domain.BaseTimeEntity;
import com.cheffi.tag.domain.Tag;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
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
	private String text;

	private int ratingCnt;

	private LocalDateTime timeToLock;

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

	Review(String title, String text, int lockAfterHours, Restaurant restaurant, Avatar writer) {
		this.title = title;
		this.text = text;
		this.timeToLock = LocalDateTime.now().plusHours(lockAfterHours);
		this.restaurant = restaurant;
		this.writer = writer;
		this.ratingCnt = 0;
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

	public boolean isLocked() {
		return LocalDateTime.now().isAfter(timeToLock);
	}
}

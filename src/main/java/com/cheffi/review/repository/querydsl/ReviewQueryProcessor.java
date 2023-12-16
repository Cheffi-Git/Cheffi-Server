package com.cheffi.review.repository.querydsl;

import static com.cheffi.avatar.domain.QPurchasedItem.*;
import static com.cheffi.review.domain.QBookmark.*;
import static com.cheffi.review.domain.QReview.*;
import static com.cheffi.review.domain.QReviewPhoto.*;

import com.cheffi.review.dto.QReviewInfoDto;
import com.cheffi.review.dto.QReviewPhotoInfoDto;
import com.cheffi.review.dto.ReviewInfoDto;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;

public class ReviewQueryProcessor {

	// 없음
	private final Long viewerId;
	private final boolean requestBookmark;
	private final boolean requestPurchase;
	private final Expression<Boolean> writerExp;
	private final Expression<Boolean> bookmarkExp;
	private final Expression<Boolean> purchaseExp;

	private ReviewQueryProcessor(Long viewerId, boolean requestBookmark, boolean requestPurchase,
		Expression<Boolean> bookmarkExp, Expression<Boolean> purchaseExp,
		Expression<Boolean> writerExp) {
		this.viewerId = viewerId;
		this.requestBookmark = requestBookmark;
		this.requestPurchase = requestPurchase;
		this.writerExp = writerExp;
		this.bookmarkExp = bookmarkExp;
		this.purchaseExp = purchaseExp;
	}

	public JPAQuery<ReviewInfoDto> process(JPAQuery<?> query) {
		var result = query.select(new QReviewInfoDto(
				review.id,
				review.title,
				review.text,
				new QReviewPhotoInfoDto(reviewPhoto.id, reviewPhoto.givenOrder, reviewPhoto.url),
				review.timeToLock,
				review.viewCnt,
				review.status,
				writerExp,
				requestBookmark ? bookmark.isNotNull() : bookmarkExp,
				requestPurchase ? purchasedItem.isNotNull() : purchaseExp
			)).leftJoin(review.photos, reviewPhoto)
			.on(photoOrderEq(0));

		if (requestBookmark)
			result.leftJoin(bookmark)
				.on(bookmark.review.id.eq(review.id),
					bookmarkWriterEq(viewerId));

		if (requestPurchase)
			result.leftJoin(purchasedItem)
				.on(purchasedItem.review.eq(review),
					purchasedItem.avatar.id.eq(viewerId));

		return result;
	}

	private BooleanExpression bookmarkWriterEq(Long viewerId) {
		return bookmark.avatar.id.eq(viewerId);
	}

	private BooleanExpression photoOrderEq(Integer givenOrder) {
		return reviewPhoto.givenOrder.eq(givenOrder);
	}

	public static Builder builder(boolean include, Long viewerId, Expression<Boolean> defaultExp) {
		return new Builder(include, viewerId, defaultExp);
	}

	public static Builder defaultBuilder(Long viewerId) {
		return ReviewQueryProcessor.builder(true, viewerId, Expressions.FALSE)
			.includeBookmark(viewerId != null, Expressions.FALSE)
			.includePurchase(viewerId != null, Expressions.FALSE);
	}

	public static class Builder {
		// 필수 매개 변수
		private final Long viewerId;

		// 선택 매개 변수
		private boolean requestBookmark = false;
		private boolean requestPurchase = false;

		private Expression<Boolean> writerExp = Expressions.FALSE;
		private Expression<Boolean> bookmarkExp = Expressions.FALSE;
		private Expression<Boolean> purchaseExp = Expressions.FALSE;

		private Builder(boolean include, Long viewerId, Expression<Boolean> defaultExp) {
			if (defaultExp != null)
				this.writerExp = defaultExp;
			if (viewerId == null || !include) {
				this.viewerId = viewerId;
				return;
			}

			this.viewerId = viewerId;
			this.writerExp = review.writer.id.eq(viewerId);
		}

		public Builder includeBookmark(boolean include, Expression<Boolean> defaultExp) {
			requestBookmark = include;
			if (!include)
				bookmarkExp = defaultExp;

			return this;
		}

		public Builder includePurchase(boolean include, Expression<Boolean> defaultExp) {
			requestPurchase = include;
			if (!include)
				purchaseExp = defaultExp;

			return this;
		}

		public ReviewQueryProcessor build() {
			if (viewerId == null) {
				return new ReviewQueryProcessor(null, false, false, bookmarkExp, purchaseExp, writerExp);
			}
			return new ReviewQueryProcessor(viewerId, requestBookmark, requestPurchase, bookmarkExp, purchaseExp,
				writerExp);
		}
	}

}

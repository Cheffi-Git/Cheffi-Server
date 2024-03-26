package com.cheffi.review.repository.querydsl;

import static com.cheffi.review.domain.QReview.*;
import static com.cheffi.review.domain.QReviewPhoto.*;

import com.cheffi.avatar.domain.QPurchasedItem;
import com.cheffi.review.constant.ReviewStatus;
import com.cheffi.review.dto.QReviewInfoDto;
import com.cheffi.review.dto.QReviewPhotoInfoDto;
import com.cheffi.review.dto.ReviewInfoDto;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;

public class ReviewQueryProcessor {

	private static final QPurchasedItem viewerPurchase = new QPurchasedItem("viewerPurchase");
	private static final QPurchasedItem viewerBookmark = new QPurchasedItem("viewerBookmark");

	private final Long viewerId;
	private final boolean requestBookmark;
	private final boolean requestPurchase;
	private final boolean onlyActive;
	private final Expression<Boolean> writerExp;
	private final Expression<Boolean> bookmarkExp;
	private final Expression<Boolean> purchaseExp;
	private final Expression<Long> cursorExp;

	private ReviewQueryProcessor(Long viewerId, boolean requestBookmark, boolean requestPurchase,
		boolean onlyActive, Expression<Boolean> bookmarkExp, Expression<Boolean> purchaseExp,
		Expression<Boolean> writerExp, Expression<Long> cursorExp) {
		this.viewerId = viewerId;
		this.requestBookmark = requestBookmark;
		this.requestPurchase = requestPurchase;
		this.onlyActive = onlyActive;
		this.writerExp = writerExp;
		this.bookmarkExp = bookmarkExp;
		this.purchaseExp = purchaseExp;
		this.cursorExp = cursorExp;
	}

	public JPAQuery<ReviewInfoDto> process(JPAQuery<?> query) {
		var result = getSelectQuery(query, cursorExp)
			.leftJoin(review.photos, reviewPhoto)
			.on(photoOrderEq(0));

		if (onlyActive)
			result.where(review.status.eq(ReviewStatus.ACTIVE));

		if (requestBookmark)
			result.leftJoin(viewerBookmark)
				.on(viewerBookmark.review.id.eq(review.id),
					bookmarkWriterEq(viewerId));

		if (requestPurchase)
			result.leftJoin(viewerPurchase)
				.on(viewerPurchase.review.eq(review),
					viewerPurchase.avatar.id.eq(viewerId));

		return result;
	}

	private JPAQuery<ReviewInfoDto> getSelectQuery(JPAQuery<?> query, Expression<Long> cursorExp) {
		return query.select(new QReviewInfoDto(
			review.id,
			review.title,
			review.text,
			new QReviewPhotoInfoDto(reviewPhoto.id, reviewPhoto.givenOrder, reviewPhoto.url),
			review.timeToLock,
			review.viewCnt,
			review.status,
			writerExp,
			requestBookmark ? viewerBookmark.isNotNull() : bookmarkExp,
			requestPurchase ? viewerPurchase.isNotNull() : purchaseExp,
			cursorExp
		));
	}

	private BooleanExpression bookmarkWriterEq(Long viewerId) {
		return viewerBookmark.avatar.id.eq(viewerId);
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
		private boolean onlyActive = true;

		private Expression<Boolean> writerExp = Expressions.FALSE;
		private Expression<Boolean> bookmarkExp = Expressions.FALSE;
		private Expression<Boolean> purchaseExp = Expressions.FALSE;
		private Expression<Long> cursorExp = Expressions.nullExpression();

		private Builder(boolean include, Long viewerId, Expression<Boolean> defaultExp) {
			if (viewerId == null) {
				this.viewerId = null;
				return;
			}

			if (!include) {
				this.viewerId = viewerId;
				if (defaultExp != null)
					this.writerExp = defaultExp;
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

		public Builder cursor(Expression<Long> expression) {
			cursorExp = expression;
			return this;
		}

		public Builder withInactive() {
			onlyActive = false;
			return this;
		}

		public ReviewQueryProcessor build() {
			if (viewerId == null) {
				return new ReviewQueryProcessor(null, false, false, onlyActive, bookmarkExp, purchaseExp, writerExp,
					cursorExp);
			}
			return new ReviewQueryProcessor(viewerId, requestBookmark, requestPurchase, onlyActive, bookmarkExp,
				purchaseExp,
				writerExp, cursorExp);
		}
	}

}

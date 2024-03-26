package com.cheffi.review.repository;

import static com.cheffi.avatar.domain.QPurchasedItem.*;
import static com.cheffi.review.domain.QBookmark.*;
import static com.cheffi.review.domain.QMenu.*;
import static com.cheffi.review.domain.QRestaurant.*;
import static com.cheffi.review.domain.QReview.*;
import static com.cheffi.review.domain.QReviewTag.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cheffi.common.constant.Address;
import com.cheffi.review.domain.Review;
import com.cheffi.review.dto.AddressSearchRequest;
import com.cheffi.review.dto.MenuSearchRequest;
import com.cheffi.review.dto.ReviewCursor;
import com.cheffi.review.dto.ReviewInfoDto;
import com.cheffi.review.dto.ReviewSearchCondition;
import com.cheffi.review.dto.request.GetMyPageReviewRequest;
import com.cheffi.review.repository.querydsl.ReviewQueryProcessor;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
public class ReviewJpaRepository {

	private final EntityManager em;
	private final JPAQueryFactory queryFactory;

	public ReviewJpaRepository(EntityManager em) {
		this.em = em;
		this.queryFactory = new JPAQueryFactory(em);
	}

	public List<ReviewInfoDto> findAllById(List<Long> ids, Long viewerId) {
		if (ids.isEmpty())
			return List.of();

		JPAQuery<?> common = queryFactory
			.from(review)
			.where(reviewIdIn(ids));

		return ReviewQueryProcessor.defaultBuilder(viewerId)
			.withInactive()
			.build()
			.process(common).fetch();
	}

	public List<Review> findByCondition(ReviewSearchCondition condition) {
		JPAQuery<Review> query = queryFactory
			.select(review)
			.from(restaurant)
			.where(
				restaurantAddressEq(condition.getAddress())
			)
			.join(review)
			.on(restaurant.eq(review.restaurant));

		Long tagId = condition.getTagId();
		if (tagId != null)
			query = query.rightJoin(reviewTag)
				.on(reviewTag.review.eq(review),
					reviewTag.tag.id.eq(tagId));

		return query.fetch();
	}

	public List<ReviewInfoDto> findByMenu(MenuSearchRequest request, Long viewerId) {
		ReviewCursor cursor = request.getCursor();

		JPAQuery<?> common = queryFactory
			.from(review)
			.distinct()
			.innerJoin(review.menus, menu)
			.on(menu.name.startsWith(request.getMenu()))
			.where(review.viewCnt.lt(cursor.getCount())
				.or(review.viewCnt.eq(cursor.getCount()).and(review.id.loe(cursor.getId())))
			)
			.orderBy(review.viewCnt.desc(), review.id.desc())
			.limit(request.getSize() + 1L);

		return ReviewQueryProcessor.defaultBuilder(viewerId).build()
			.process(common).fetch();
	}

	public List<ReviewInfoDto> findByAddress(AddressSearchRequest request, Long viewerId) {
		ReviewCursor cursor = request.getCursor();

		JPAQuery<?> common = queryFactory
			.from(review)
			.leftJoin(review.restaurant, restaurant)
			.where(review.viewCnt.lt(cursor.getCount())
					.or(review.viewCnt.eq(cursor.getCount()).and(review.id.loe(cursor.getId())))
				, restaurantAddressEq(request.getAddress())
			)
			.orderBy(review.viewCnt.desc(), review.id.desc())
			.limit(request.getSize() + 1L);

		return ReviewQueryProcessor.defaultBuilder(viewerId).build()
			.process(common).fetch();
	}

	public List<ReviewInfoDto> findByWriter(GetMyPageReviewRequest request, Long writerId, Long viewerId) {
		JPAQuery<?> query = queryFactory
			.from(review)
			.where(review.writer.id.eq(writerId)
				.and(review.id.loe(request.getCursor(Long.MAX_VALUE))))
			.orderBy(review.id.desc())
			.limit(request.getSize() + 1L);

		boolean authenticated = viewerId != null;
		boolean isWriter = writerId.equals(viewerId);

		return ReviewQueryProcessor.builder(false, viewerId, isWriter ? Expressions.TRUE : Expressions.FALSE)
			.includePurchase(authenticated && !isWriter, Expressions.FALSE)
			.includeBookmark(false, Expressions.nullExpression())
			.build()
			.process(query).fetch();
	}

	public List<ReviewInfoDto> findByBookmarks(GetMyPageReviewRequest request, Long ownerId, Long viewerId) {
		JPAQuery<?> query = queryFactory
			.from(bookmark)
			.leftJoin(bookmark.review, review)
			.where(bookmark.avatar.id.eq(ownerId)
				.and(bookmark.id.loe(request.getCursor(Long.MAX_VALUE))))
			.orderBy(bookmark.id.desc())
			.limit(request.getSize() + 1L);

		boolean authenticated = viewerId != null;
		boolean isOwner = ownerId.equals(viewerId);

		return ReviewQueryProcessor.builder(authenticated, viewerId, null)
			.includePurchase(authenticated, Expressions.FALSE)
			.includeBookmark(false, isOwner ? Expressions.TRUE : Expressions.nullExpression())
			.cursor(bookmark.id)
			.build()
			.process(query).fetch();
	}

	public List<ReviewInfoDto> findByPurchaser(GetMyPageReviewRequest request, Long purchaserId, Long viewerId) {
		JPAQuery<?> query = queryFactory
			.from(purchasedItem)
			.leftJoin(purchasedItem.review, review)
			.where(purchasedItem.avatar.id.eq(purchaserId)
				.and(purchasedItem.id.loe(request.getCursor(Long.MAX_VALUE))))
			.orderBy(purchasedItem.id.desc())
			.limit(request.getSize() + 1L);

		boolean authenticated = viewerId != null;
		boolean isNotPurchaser = !purchaserId.equals(viewerId);

		return ReviewQueryProcessor.builder(authenticated && isNotPurchaser, viewerId, Expressions.FALSE)
			.includePurchase(authenticated && isNotPurchaser, authenticated ? Expressions.TRUE : Expressions.FALSE)
			.includeBookmark(false, Expressions.nullExpression())
			.cursor(purchasedItem.id)
			.build()
			.process(query).fetch();
	}

	private static BooleanExpression restaurantAddressEq(Address address) {
		return restaurant.detailedAddress.province.eq(address.getProvince())
			.and(restaurant.detailedAddress.city.eq(address.getCity()));
	}

	private static BooleanExpression reviewIdIn(List<Long> reviewIds) {
		return review.id.in(reviewIds);
	}

}

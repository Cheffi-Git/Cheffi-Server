package com.cheffi.avatar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cheffi.avatar.domain.PurchasedItem;

public interface PurchasedItemRepository extends JpaRepository<PurchasedItem, Long> {

	@Query("select (count(p) > 0) from PurchasedItem p "
		+ "where p.avatar.id = :avatar "
		+ "and p.review.id = :review ")
	Boolean existByAvatarAndReview(@Param("avatar") Long avatarId, @Param("review") Long reviewId);

	@Query("select p from PurchasedItem p "
		+ "where p.avatar.id = :avatar")
	List<PurchasedItem> findByAvatar(@Param("avatar") Long avatarId);

}

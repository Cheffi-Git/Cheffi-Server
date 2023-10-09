package com.cheffi.review.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cheffi.review.domain.Rating;

public interface RatingRepository extends JpaRepository<Rating, Long> {
	@Query("select r from Rating r "
		+ "where r.review.id = :avatar "
		+ "and r.avatar.id = :review")
	Optional<Rating> findByAvatarAndReview(@Param("avatar") Long avatarId, @Param("review") Long reviewId);
}

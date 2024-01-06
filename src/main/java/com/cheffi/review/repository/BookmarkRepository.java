package com.cheffi.review.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cheffi.review.domain.Bookmark;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
	@Query("select (count(b) > 0) from Bookmark b "
		+ "where b.avatar.id = :avatar "
		+ "and b.review.id = :review")
	boolean existsByAvatarAndReview(@Param("avatar") Long avatarId, @Param("review") Long reviewId);

	@Query("select b from Bookmark b "
		+ "where b.avatar.id = :avatar "
		+ "and b.review.id = :review")
	Optional<Bookmark> findByAvatarAndReview(@Param("avatar") Long avatarId, @Param("review") Long reviewId);

}

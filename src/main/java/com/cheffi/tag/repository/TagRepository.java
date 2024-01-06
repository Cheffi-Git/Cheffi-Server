package com.cheffi.tag.repository;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cheffi.tag.constant.TagType;
import com.cheffi.tag.domain.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {

	@Cacheable(cacheNames = "Tag", key = "new org.springframework.cache.interceptor.SimpleKey(#type)"
		, cacheManager = "localCacheManager")
	List<Tag> findByTagType(TagType type);

	@Cacheable(cacheNames = "Tag", cacheManager = "localCacheManager")
	List<Tag> findAll();

	@Query("select t from Review r "
		+ "left join r.reviewTags rt "
		+ "left join rt.tag t "
		+ "where r.id = :review ")
	List<Tag> findByReviewId(@Param("review") Long reviewId);

	@Query("select t from Avatar a "
		+ "left join a.avatarTags at "
		+ "left join at.tag t "
		+ "where a.id = :avatar ")
	List<Tag> findByAvatarId(@Param("avatar") Long avatarId);
}

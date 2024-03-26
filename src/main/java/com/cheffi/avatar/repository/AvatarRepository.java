package com.cheffi.avatar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cheffi.avatar.domain.Avatar;
import com.cheffi.user.domain.User;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {
	@Query("select a from Avatar a "
		+ "where a.user.activated = true")
	List<Avatar> findAllActive();

	@Query("select a from Avatar a"
		+ " left join fetch a.photo p"
		+ " where a.id = :id")
	Optional<Avatar> findByIdWithPhoto(@Param("id") Long avatarId);

	@Query("select a from Avatar a"
		+ " left join fetch a.photo p"
		+ " where a.user = :user")
	Optional<Avatar> findByUserWithPhoto(@Param("user") User user);

	boolean existsByNickname(String nickname);

	@Query("select a from Avatar a"
		+ " left join fetch a.avatarTags at"
		+ " left join fetch at.tag t"
		+ " where a.id = :id")
	Optional<Avatar> findByIdWithTags(@Param("id") Long avatarId);

	@Query("select a from Avatar a"
		+ " left join fetch a.photo p"
		+ " left join fetch a.avatarTags at"
		+ " left join fetch at.tag t"
		+ " where a.id = :id")
	Optional<Avatar> findByIdWithTagsAndPhoto(@Param("id") Long avatarId);

}

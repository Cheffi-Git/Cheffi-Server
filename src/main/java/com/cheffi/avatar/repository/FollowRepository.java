package com.cheffi.avatar.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cheffi.avatar.domain.Avatar;
import com.cheffi.avatar.domain.Follow;
import com.cheffi.avatar.repository.querydsl.FollowCustomRepository;

public interface FollowRepository extends JpaRepository<Follow, Long>, FollowCustomRepository {

	Optional<Follow> findBySubjectAndTarget(Avatar subject, Avatar target);

	boolean existsBySubjectAndTarget(Avatar subject, Avatar target);
}

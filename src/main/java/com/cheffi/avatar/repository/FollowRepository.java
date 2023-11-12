package com.cheffi.avatar.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cheffi.avatar.domain.Avatar;
import com.cheffi.avatar.domain.Follow;

public interface FollowRepository extends JpaRepository<Follow, Long>{

	Optional<Follow> findBySubjectAndTarget(Avatar subject, Avatar target);

	boolean existsBySubjectAndTarget(Avatar subject, Avatar target);
}

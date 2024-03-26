package com.cheffi.avatar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cheffi.avatar.domain.Avatar;
import com.cheffi.avatar.domain.Follow;

public interface FollowRepository extends JpaRepository<Follow, Long> {

	Optional<Follow> findBySubjectAndTarget(Avatar subject, Avatar target);

	@Query("""
		select (count(f) > 0)
		from Follow f
		where f.subject.id = :subject
		and f.target.id = :target
		""")
	boolean existsBySubjectAndTarget(@Param("subject") Long subject, @Param("target") Long target);

	@Query("""
		select a from Follow f
		join f.subject a
		where f.target.id = :target
		""")
	List<Avatar> findFollowerByTarget(@Param("target") Long target);

}

package com.cheffi.cs.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cheffi.cs.domain.Block;

public interface BlockRepository extends JpaRepository<Block, Long> {

	@Query(""" 
		select (count(b) > 0)
		from Block b
		where b.subject.id = :subject
		and b.target.id = :target
		""")
	boolean existsBySubjectAndTarget(@Param("subject") Long subject, @Param("target") Long target);

	@Query(""" 
		select b
		from Block b
		where b.subject.id = :subject
		and b.target.id = :target
		""")
	Optional<Block> findBySubjectAndTarget(@Param("subject") Long subject, @Param("target") Long target);

}

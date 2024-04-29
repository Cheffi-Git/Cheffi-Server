package com.cheffi.view.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cheffi.view.domain.ViewCountingFailure;

public interface ViewCountingFailureRepository extends JpaRepository<ViewCountingFailure, Long> {

	@Query("select v from ViewCountingFailure v where v.id = :id")
	Optional<ViewCountingFailure> findById(@Param("id") Long id);

}

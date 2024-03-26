package com.cheffi.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cheffi.review.domain.ViewHistory;

public interface ViewHistoryRepository extends JpaRepository<ViewHistory, Long> {
}

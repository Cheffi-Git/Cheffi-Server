package com.cheffi.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cheffi.review.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}

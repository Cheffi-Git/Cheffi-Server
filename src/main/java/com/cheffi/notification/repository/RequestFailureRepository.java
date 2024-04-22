package com.cheffi.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cheffi.notification.domain.RequestFailure;

public interface RequestFailureRepository extends JpaRepository<RequestFailure, Long> {
}

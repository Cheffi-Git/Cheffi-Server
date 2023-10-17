package com.cheffi.avatar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cheffi.avatar.domain.CheffiCoin;

public interface CheffiCoinRepository extends JpaRepository<CheffiCoin, Long> {
}

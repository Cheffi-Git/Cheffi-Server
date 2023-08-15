package com.cheffi.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cheffi.user.constant.RoleType;
import com.cheffi.user.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

	@Query("select r from Role r where r.roleType = :roleType")
	Optional<Role> findByAuthority(RoleType roleType);

}

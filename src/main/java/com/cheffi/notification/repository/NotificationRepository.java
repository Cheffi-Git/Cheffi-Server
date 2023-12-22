package com.cheffi.notification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cheffi.notification.domain.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	@Query("""
		select n
		from Notification n
		where n.avatar.id = :avatar""")
	List<Notification> findAllByAvatar(@Param("avatar") Long avatarId);

}

package com.cheffi.notification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import com.cheffi.notification.domain.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	@Query("""
		select n
		from Notification n
		where n.target.id = :target
		""")
	List<Notification> findAllByAvatar(@Param("target") Long avatarId);

	@Query("""
		select n
		from Notification n
		where n.target.id = :target
		and n.id in :ids
		""")
	List<Notification> findByAvatarAndId(@Param("target") Long avatarId, @Param("ids") List<Long> ids);

	@Query("""
		select (count(n) > 0)
		from Notification n
		where n.checked = false
		and n.target.id = :target
		""")
	boolean existsUncheckedByAvatar(@Param("target") @NonNull Long avatar);
}

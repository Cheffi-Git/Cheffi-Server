package com.cheffi.notice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cheffi.notice.domain.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
	@Query("select n from Notice n "
		+ "where n.closed = false")
	Page<Notice> findOpenNotices(Pageable pageable);
}

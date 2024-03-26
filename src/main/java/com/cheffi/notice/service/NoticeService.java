package com.cheffi.notice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.notice.dto.NoticeDto;
import com.cheffi.notice.repository.NoticeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NoticeService {

	private final NoticeRepository noticeRepository;

	public Page<NoticeDto> getNotices(Pageable pageable) {
		return noticeRepository.findOpenNotices(pageable).map(NoticeDto::new);
	}

}

package com.cheffi.cs.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.cs.constant.QnaCategory;
import com.cheffi.cs.dto.QnaDto;
import com.cheffi.cs.repository.QnaJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class QnaService {

	private final QnaJpaRepository qnaJpaRepository;

	public List<QnaDto> getByCategory(QnaCategory category) {
		return qnaJpaRepository.findByCategory(category).stream().map(QnaDto::of).toList();
	}
}

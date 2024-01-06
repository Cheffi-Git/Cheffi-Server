package com.cheffi.cs.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.avatar.service.AvatarService;
import com.cheffi.avatar.service.FollowService;
import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.BusinessException;
import com.cheffi.common.dto.CursorPage;
import com.cheffi.cs.domain.Block;
import com.cheffi.cs.dto.DeleteBlockRequest;
import com.cheffi.cs.dto.GetBlockRequest;
import com.cheffi.cs.dto.GetBlockResponse;
import com.cheffi.cs.dto.PostBlockRequest;
import com.cheffi.cs.repository.BlockJpaRepository;
import com.cheffi.cs.repository.BlockRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BlockService {

	private final BlockRepository blockRepository;
	private final BlockJpaRepository blockJpaRepository;
	private final AvatarService avatarService;
	private final FollowService followService;

	@Transactional
	public Long block(PostBlockRequest request, Long subjectId) {
		if (blockRepository.existsBySubjectAndTarget(subjectId, request.id()))
			throw new BusinessException(ErrorCode.ALREADY_BLOCKED);
		// 팔로우 관계 삭제
		followService.destroyFriendship(subjectId, request.id());
		return blockRepository.save(Block.of(avatarService.getById(subjectId), avatarService.getById(request.id())))
			.getId();
	}

	@Transactional
	public void unblock(DeleteBlockRequest request, Long subjectId) {
		if (subjectId.equals(request.id()))
			throw new BusinessException(ErrorCode.CANNOT_UNBLOCK_SELF);
		Block block = blockRepository.findBySubjectAndTarget(subjectId, request.id())
			.orElseThrow(() -> new BusinessException(ErrorCode.NOT_BLOCKED_AVATAR));
		blockRepository.delete(block);
	}

	public CursorPage<GetBlockResponse, Long> getBlockList(GetBlockRequest request, Long subjectId) {
		return CursorPage.of(blockJpaRepository.findBlockList(request, subjectId), request.getSize(),
			GetBlockResponse::cursor);
	}

	public boolean checkBlocked(Long subjectId, Long targetId) {
		return blockRepository.existsBySubjectAndTarget(subjectId, targetId);
	}
}

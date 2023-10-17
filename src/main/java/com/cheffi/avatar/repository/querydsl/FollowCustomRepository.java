package com.cheffi.avatar.repository.querydsl;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.cheffi.avatar.domain.Follow;
import com.cheffi.common.request.CursorPageable;

public interface FollowCustomRepository {

	List<Follow> getFollows(Long avatarId, CursorPageable pageable);

	boolean existsFollowsAfterId(@Param("follow") Long followId);
}
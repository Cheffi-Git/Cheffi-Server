package com.cheffi.cs.repository;

import static com.cheffi.avatar.domain.QAvatar.*;
import static com.cheffi.avatar.domain.QProfilePhoto.*;
import static com.cheffi.cs.domain.QBlock.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cheffi.cs.dto.GetBlockRequest;
import com.cheffi.cs.dto.GetBlockResponse;
import com.cheffi.cs.dto.QGetBlockResponse;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
public class BlockJpaRepository {

	private final EntityManager em;
	private final JPAQueryFactory queryFactory;

	public BlockJpaRepository(EntityManager em) {
		this.em = em;
		this.queryFactory = new JPAQueryFactory(em);
	}

	public List<GetBlockResponse> findBlockList(GetBlockRequest request, Long subjectId) {

		JPAQuery<GetBlockResponse> query = queryFactory.select(
				new QGetBlockResponse(
					block.id,
					avatar.id,
					avatar.nickname,
					profilePhoto.url,
					block.createdDate
				)
			).from(block)
			.leftJoin(block.target, avatar)
			.leftJoin(avatar.photo, profilePhoto)
			.where(block.subject.id.eq(subjectId),
				block.id.loe(request.getCursor(Long.MAX_VALUE)))
			.orderBy(block.id.desc())
			.limit(request.getSize() + 1L);

		return query.fetch();
	}
}

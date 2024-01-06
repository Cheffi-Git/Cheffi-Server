package com.cheffi.review.domain;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.ZSetOperations;

import com.cheffi.review.dto.ReviewTuple;
import com.cheffi.review.dto.dao.ScoreDto;

public class ReviewRanking {
	private final List<ReviewScore> reviewScores;

	public ReviewRanking(List<Review> reviews) {
		this.reviewScores = reviews.stream().map(ReviewScore::new).toList();
	}

	public List<Long> getIds() {
		return reviewScores.stream().map(ReviewScore::getId).toList();
	}

	public void includeScore(List<ScoreDto> scoreDtos) {
		for (ScoreDto r : scoreDtos) {
			Long id = r.getId();
			for (ReviewScore rs : reviewScores) {
				if (id.equals(rs.getId())) {
					rs.addRatingScore(r);
					break;
				}
			}
		}
	}

	public Set<ZSetOperations.TypedTuple<Object>> toTuples() {
		return reviewScores.stream()
			.map(rs -> ReviewTuple.of(rs.getId(), rs.getScore()))
			.collect(Collectors.toUnmodifiableSet());
	}

	private static class ReviewScore {
		private final Review review;
		private int score;

		public ReviewScore(Review review) {
			this.review = review;
			this.score = 0;
		}

		public Long getId() {
			return review.getId();
		}

		public void addRatingScore(ScoreDto scoreDto) {
			score += scoreDto.getCount();
		}

		public Double getScore() {
			return (double)this.score;
		}
	}

}

package com.cheffi;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import com.cheffi.avatar.dto.GetFollowRequest;
import com.cheffi.avatar.dto.response.GetFollowResponse;
import com.cheffi.avatar.dto.response.RecommendFollowResponse;
import com.cheffi.avatar.repository.FollowJpaRepository;
import com.cheffi.avatar.service.FollowService;
import com.cheffi.common.constant.Address;
import com.cheffi.common.dto.CursorPage;
import com.cheffi.review.dto.MenuSearchRequest;
import com.cheffi.review.dto.ReviewInfoDto;
import com.cheffi.review.dto.dao.ScoreDto;
import com.cheffi.review.repository.RatingJpaRepository;
import com.cheffi.review.repository.ReviewJpaRepository;
import com.cheffi.review.repository.ReviewRepository;
import com.cheffi.review.repository.ViewHistoryJpaRepository;
import com.cheffi.tag.service.TagService;

@SpringBootTest
class CheffiApplicationTests {

	private final RedisTemplate<String, Object> redisTemplate;

	private final RatingJpaRepository ratingJpaRepository;
	private final ViewHistoryJpaRepository viewHistoryJpaRepository;
	private final ReviewJpaRepository reviewJpaRepository;
	private final ReviewRepository reviewRepository;
	private final DateTimeFormatter formatter;
	private final TagService tagService;
	private final FollowJpaRepository followJpaRepository;
	private final FollowService followService;

	@Autowired
	CheffiApplicationTests(RedisTemplate<String, Object> redisTemplate, RatingJpaRepository ratingJpaRepository,
		ViewHistoryJpaRepository viewHistoryJpaRepository, ReviewJpaRepository reviewJpaRepository,
		ReviewRepository reviewRepository, TagService tagService, FollowJpaRepository followJpaRepository,
		FollowService followService) {
		this.redisTemplate = redisTemplate;
		this.ratingJpaRepository = ratingJpaRepository;
		this.viewHistoryJpaRepository = viewHistoryJpaRepository;
		this.reviewJpaRepository = reviewJpaRepository;
		this.reviewRepository = reviewRepository;
		this.tagService = tagService;
		this.followJpaRepository = followJpaRepository;
		this.followService = followService;
		this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH");
	}

	@Test
	void contextLoads() {
		Address address = Address.cityAddress("서울특별시", "마포구");
		// String key = address.getCombined() + LocalDateTime.now().format(formatter);
		String key = "test";
		ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();
		zSetOps.add(key, 20L, 50);
		zSetOps.add(key, 35L, 150);
		zSetOps.add(key, 10L, 100);

		Set<ZSetOperations.TypedTuple<Object>> tuples = zSetOps.reverseRangeWithScores(key, 0, 10);

		for (var a : tuples) {
			System.out.println("a = " + a);
		}

		RedisOperations<String, Object> operations = zSetOps.getOperations();
		operations.expire(key, Duration.ofHours(1));
	}

	@Test
	void test() {
		String key = "test";
		ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();
		Set<Object> range = zSetOps.rangeByScore(key, 2, 1);
		if (range.isEmpty()) {
			Boolean hasKey = zSetOps.getOperations().hasKey(key);
			System.out.println("hasKey = " + hasKey);
		}
		System.out.println("range.size() = " + range.size());
		range.forEach(System.out::println);
	}

	@Test
	void test2() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime minusDays = now.minusHours(11);
		System.out.println("now = " + now);
		List<ScoreDto> scoreDtos = ratingJpaRepository.countBetween(List.of(4L, 5L),
			minusDays,
			now);
		for (ScoreDto r :
			scoreDtos) {
			System.out.println("r = " + r);
		}
	}

	@Test
	void test3() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime minusDays = now.minusHours(11);
		System.out.println("minusDays = " + minusDays);
		System.out.println("now = " + now);
		List<ScoreDto> scoreDtos = viewHistoryJpaRepository.countBetween(List.of(4L, 5L),
			minusDays,
			now);
		for (ScoreDto r :
			scoreDtos) {
			System.out.println("r = " + r);
		}
	}

	@Test
	void test4() {
		List<ReviewInfoDto> allById = reviewJpaRepository.findAllById(List.of(4L, 5L), null);
		for (var r :
			allById) {
			System.out.println("r = " + r);
		}
	}

	@Test
	void test5() {
		List<ReviewInfoDto> allById = reviewJpaRepository.findAllById(List.of(4L, 5L), 1L);
		for (var r :
			allById) {
			System.out.println("r = " + r);
		}
	}

	@Test
	void test6() {
		List<?> reviews = reviewJpaRepository.findByMenu(
			new MenuSearchRequest(Long.MAX_VALUE, Integer.MAX_VALUE, "짜장", 10), 1L);
		for (var r : reviews) {
			System.out.println("r = " + r);
		}
	}

	@Test
	void test7() {
		List<RecommendFollowResponse> responses1 = followJpaRepository.recommendByTag(1L, 1L);
		List<RecommendFollowResponse> responses2 = followJpaRepository.recommendByTag(1L, 2L);
		List<RecommendFollowResponse> responses3 = followJpaRepository.recommendByTag(1L, 3L);
		for (var list : List.of(responses1, responses2, responses3)) {
			for (var r : list) {
				System.out.println("r = " + r);
			}
		}
	}

	@Test
	void test8() {
		List<GetFollowResponse> responses1 = followJpaRepository.findFollowing(new GetFollowRequest(null, 10), 1L,
			null);
		List<GetFollowResponse> responses2 = followJpaRepository.findFollowing(new GetFollowRequest(null, 10), 2L,
			null);
		List<GetFollowResponse> responses3 = followJpaRepository.findFollowing(new GetFollowRequest(null, 10), 3L,
			null);
		for (var list : List.of(responses1, responses2, responses3)) {
			for (var r : list) {
				System.out.println("r = " + r);
			}
			System.out.println();
		}
	}

	@Test
	void test8V() {
		List<GetFollowResponse> responses1 = followJpaRepository.findFollowing(new GetFollowRequest(null, 10), 1L, 2L);
		List<GetFollowResponse> responses2 = followJpaRepository.findFollowing(new GetFollowRequest(null, 10), 2L, 2L);
		List<GetFollowResponse> responses3 = followJpaRepository.findFollowing(new GetFollowRequest(null, 10), 3L, 2L);
		for (var list : List.of(responses1, responses2, responses3)) {
			for (var r : list) {
				System.out.println("r = " + r);
			}
			System.out.println();
		}
	}

	@Test
	void test9() {
		List<GetFollowResponse> responses1 = followJpaRepository.findFollower(new GetFollowRequest(null, 10), 1L, null);
		List<GetFollowResponse> responses2 = followJpaRepository.findFollower(new GetFollowRequest(null, 10), 2L, null);
		List<GetFollowResponse> responses3 = followJpaRepository.findFollower(new GetFollowRequest(null, 10), 3L, null);
		for (var list : List.of(responses1, responses2, responses3)) {
			for (var r : list) {
				System.out.println("r = " + r);
			}
			System.out.println();
		}
	}

	@Test
	void test9V() {
		List<GetFollowResponse> responses1 = followJpaRepository.findFollower(new GetFollowRequest(null, 1), 1L, 2L);
		List<GetFollowResponse> responses2 = followJpaRepository.findFollower(new GetFollowRequest(null, 1), 2L, 2L);
		List<GetFollowResponse> responses3 = followJpaRepository.findFollower(new GetFollowRequest(null, 1), 3L, 2L);
		for (var list : List.of(responses1, responses2, responses3)) {
			for (var r : list) {
				System.out.println("r = " + r);
			}
			System.out.println();
		}
	}

	@Test
	void test10V() {
		CursorPage<GetFollowResponse, Long> responses1 = followService.getFollower(new GetFollowRequest(null, 3), 1L,
			1L);

		for (var result : responses1.getData()) {
			System.out.println(result.cursor() + "   " + result.id() + "   " + result.following());
		}

		System.out.println("responses1.getNext() = " + responses1.getNext());

	}

	@Test
	void test11V() {
		CursorPage<GetFollowResponse, Long> responses1 = followService.getFollowing(new GetFollowRequest(5L, 3), 1L,
			1L);
		for (var result : responses1.getData()) {
			System.out.println(result.cursor() + "   " + result.id() + "   " + result.following());
		}
		System.out.println("next = " + responses1.getNext());
	}

}

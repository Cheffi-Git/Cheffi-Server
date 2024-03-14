package com.cheffi;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.avatar.domain.Avatar;
import com.cheffi.avatar.service.AvatarService;
import com.cheffi.review.constant.RatingType;
import com.cheffi.review.domain.Rating;
import com.cheffi.review.domain.Restaurant;
import com.cheffi.review.domain.Review;
import com.cheffi.review.domain.ReviewCreateRequest;
import com.cheffi.review.domain.ReviewPhoto;
import com.cheffi.review.domain.ViewHistory;
import com.cheffi.review.repository.RatingRepository;
import com.cheffi.review.repository.RestaurantRepository;
import com.cheffi.review.repository.ReviewRepository;
import com.cheffi.review.repository.ViewHistoryRepository;

@Service
public class DummyReviewMaker {

	private static final String TEXT = "이 식감이면 집중해서 오물거릴수밖에\n"
		+ "국제선 탑승장보다 외국어가 더 잘 들리는 명동이지만 여긴 아니다. 공용어라 칭할 수 있는 감탄과 탄성, 그리고 쫀득한 면을 입에 가득 넣고 오물거리는 "
		+ "소리만이 들린다. 입에 감기는 쫄깃함이 있으니 도삭면의 개성이 더 잘 도드라진다.\n"
		+ "과장 없이 마이너리티 리포트처럼, 내가 뭘 먹을지 이미 예상하고 조리를 한 듯 메뉴가 빨리 나오기 때문에 인내심은 매콤함을 참을 때만 필요하다. "
		+ "유슬볶음자장도삭면은 기존 자장면에 고춧가루 팍팍 친 느낌의 매콤함 수준 정도고, 쯔란양고기도삭면은 ‘신라면 정도예요‘보다 알싸하다. "
		+ "김치 또한 칼국수집 김치처럼 자극적이다. 그래도 중식은 매콤함으로 입과 위장을 괴롭혀야 진짜 잘 먹은 게 아닐까? "
		+ "여기에 온 목적을 생각하면 타당한 맵기에 적당히 진한 향이다.\n"
		+ "쫀득함은 타당함을 넘는 수준이다. ‘쫀득함’이라는 개념을 몸으로 깨닫는다. 뚝뚝 끊어지는 도삭면보다 훨씬 취향이었다. "
		+ "면보다 수제비와 젤리에 가끼운 쫀득함을 가졌으면서 또 기분 나쁜 질겅거림은 없는 완벽한 밸런스다. "
		+ "이 쫀득함 때문에 내가 다른 면이 아닌, 확실하게 구분되는 도삭면을 먹는다는 사실을 깨닫는다. "
		+ "그리고는 그릇 바닥에 기름과 양념만이 남을 때까지 집중력 있게 싹싹 긁어 입에 가득 담아 오물거린다.\n"
		+ "사실 면이 양이 많은 편은 아니다. 그러니 꼭 꿔바로우도 앉자마자 같이 주문하자. 얘도 식감이 독특하니 재밌다. "
		+ "기존에 먹던 것이 꿔바로우라면 이건 꿔바로우모찌다. 똑같이 바삭하지만 이 정도 쫀득함은 처음이다. "
		+ "중식은 자극적인 맛에서 오는 매력으로 먹지만, 여긴 이 쫀득한 식감이 매력이다. 미뢰보다 턱관절이 더 감동한다. "
		+ "그렇다고 자극적이지 않은 건 아니니 위장 컨디션은 잘 보고 가자.";
	private static final String S3_KEY = "review/7cb9e1ed-9f2d-437f-935d-d2ddd9640a52/Cheffi_Photo_2023-10-17-22-07-05.png";
	private static final String PHOTO_URL =
		"https://cheffibucket.s3.ap-northeast-2.amazonaws.com/review/7cb9e1ed-9f2d-437f-"
			+ "935d-d2ddd9640a52/Cheffi_Photo_2023-10-17-22-07-05.png";
	private final RestaurantRepository restaurantRepository;
	private final ReviewRepository reviewRepository;
	private final AvatarService avatarService;
	private final ViewHistoryRepository viewHistoryRepository;
	private final RatingRepository ratingRepository;

	public DummyReviewMaker(RestaurantRepository restaurantRepository, ReviewRepository reviewRepository,
		AvatarService avatarService, ViewHistoryRepository viewHistoryRepository,
		RatingRepository ratingRepository) {
		this.restaurantRepository = restaurantRepository;
		this.reviewRepository = reviewRepository;
		this.avatarService = avatarService;
		this.viewHistoryRepository = viewHistoryRepository;
		this.ratingRepository = ratingRepository;
	}

	@Transactional
	public void makeReview(Long restaurantId) {
		Restaurant restaurant = restaurantRepository.findById(restaurantId).get();
		Avatar writer = avatarService.getById(1L);

		// 리뷰 100개 생성
		for (int j = 0; j < 10; j++) {
			String title = restaurant.getName() + "의 리뷰 " + j + "번 째";
			ReviewCreateRequest request = new ReviewCreateRequest(title, TEXT, 24);
			Review review = Review.of(request, restaurant, writer);
			// 	사진 10개
			List<ReviewPhoto> reviewPhotos = new ArrayList<>();
			for (int k = 0; k < 10; k++) {
				reviewPhotos.add(new ReviewPhoto(PHOTO_URL, S3_KEY, 18339L, 491, 512, k, review));
			}
			review.addPhotos(reviewPhotos);

			reviewRepository.save(review);

			// 	조회 50개
			for (int k = 0; k < 50; k++) {
				ViewHistory history = ViewHistory.of(writer, review);
				review.getViewHistories().add(history);
				viewHistoryRepository.save(history);
				review.read();
			}

			// 	평가 40개
			Random rd = new Random();
			int good = rd.nextInt(41);

			for (int k = 0; k < good; k++) {
				ratingRepository.save(Rating.of(writer, review, RatingType.GOOD));
			}

			int average = rd.nextInt(41 - good);
			for (int k = 0; k < average; k++) {
				ratingRepository.save(Rating.of(writer, review, RatingType.AVERAGE));
			}

			int bad = rd.nextInt(41 - good - average);
			for (int k = 0; k < bad; k++) {
				ratingRepository.save(Rating.of(writer, review, RatingType.BAD));
			}

		}

	}
}

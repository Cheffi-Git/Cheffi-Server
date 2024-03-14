package com.cheffi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
class DummyDataTest {

	private final DummyReviewMaker dummyReviewMaker;
	private final DummyRestaurantMaker dummyRestaurantMaker;

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

	@Autowired
	public DummyDataTest(DummyReviewMaker dummyReviewMaker, DummyRestaurantMaker dummyRestaurantMaker) {
		this.dummyReviewMaker = dummyReviewMaker;
		this.dummyRestaurantMaker = dummyRestaurantMaker;
	}

	@Test
	@Rollback(false)
	@WithMockCustomUser
	void saveDummyReviews() {
		// start 부터 5000개의 식당에 대한 리뷰 100개 씩 생성
		long start = 1L;
		long end = start + 5000;
		for (long i = start; i < end; i++) {
			dummyReviewMaker.makeReview(i);
		}

		System.out.println("start = " + start);
		System.out.println("end = " + (end - 1));
	}

	@Test
	@Rollback(false)
	@WithMockCustomUser
	void saveDummyRestaurant() {
		dummyRestaurantMaker.makeRestaurant(500);
	}

}

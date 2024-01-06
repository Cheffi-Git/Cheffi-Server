package com.cheffi.review.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cheffi.avatar.domain.Avatar;
import com.cheffi.common.config.exception.business.BusinessException;
import com.cheffi.tag.constant.TagType;
import com.cheffi.tag.domain.Tag;

@ExtendWith(MockitoExtension.class)
class ReviewTest {

	private static final String TITLE = "제목";
	private static final String TEXT = "리뷰 본문";
	@Mock
	private Restaurant restaurant;
	@Mock
	private Avatar writer;

	private Review review;

	@BeforeEach
	void setReview() {
		review = new Review(TITLE, TEXT, 24, restaurant, writer);
	}

	@Nested
	@DisplayName("Menu 관련 메서드")
	class AboutMenus {

		@Mock
		private Menu ramen;
		@Mock
		private Menu rice;
		@Mock
		private Menu curry;
		@Mock
		private Menu beef;
		@Mock
		private Menu pork;
		@Mock
		private Menu duck;

		@Nested
		@DisplayName("addMenus 메서드")
		class AddMenus {

			@Test
			@DisplayName("총 메뉴 개수가 5개 초과이면 실패한다. - 1")
			void givenInvalidSizeOfMenus1() {
				//given
				List<Menu> originalMenus = List.of(ramen, rice);
				List<Menu> menusToAdd = List.of(curry, beef, pork, duck);
				review.getMenus().addAll(originalMenus);

				//then
				assertThatExceptionOfType(BusinessException.class).isThrownBy(
					//when
					() -> review.addMenus(menusToAdd)
				);
			}

			@Test
			@DisplayName("총 메뉴 개수가 5개 초과이면 실패한다. - 2")
			void givenInvalidSizeOfMenus2() {
				//given
				List<Menu> menusToAdd = List.of(ramen, rice, curry, beef, pork, duck);

				//then
				assertThatExceptionOfType(BusinessException.class).isThrownBy(
					//when
					() -> review.addMenus(menusToAdd)
				);
			}

			@Test
			@DisplayName("총 메뉴 개수가 5개 이내이면 성공한다.")
			void givenValidSizeOfMenus() {
				//given
				List<Menu> originalMenus = List.of(ramen, rice);
				List<Menu> menusToAdd = List.of(beef, pork, duck);
				review.getMenus().addAll(originalMenus);

				//when
				review.addMenus(menusToAdd);

				//then
				List<Menu> menus = review.getMenus();
				assertThat(menus).hasSize(5);
				assertThat(originalMenus).isSubsetOf(menus);
				assertThat(menusToAdd).isSubsetOf(menus);
			}

		}

		@Nested
		@DisplayName("clearMenus 메서드")
		class ClearMenus {

			@Test
			@DisplayName("호출 시 리뷰에 매핑된 모든 Menu 를 제거한다.")
			void clearMenuList() {
				//given
				List<Menu> originalMenus = List.of(ramen, rice);
				review.getMenus().addAll(originalMenus);

				//when
				review.clearMenus();

				//then
				List<Menu> menus = review.getMenus();
				assertThat(menus).isEmpty();
			}

		}

	}

	@Nested
	@DisplayName("Tag 관련 메서드")
	class AboutTags {

		private static final Tag KOREAN_TAG = new Tag(TagType.FOOD, "한식");
		private static final Tag CHINESE_TAG = new Tag(TagType.FOOD, "중식");
		private static final Tag JAPANESE_TAG = new Tag(TagType.FOOD, "일식");
		private final List<Tag> TAG_LIST = List.of(KOREAN_TAG, CHINESE_TAG, JAPANESE_TAG);

		@Nested
		@DisplayName("addTags 메서드")
		class AddTags {

			@Test
			@DisplayName("중복된 태그 요청에 대해 ReviewTag 는 한 번만 추가된다.")
			void givenDuplicated() {
				//when
				review.addTags(TAG_LIST);
				review.addTags(List.of(KOREAN_TAG, CHINESE_TAG));
				review.addTags(List.of(KOREAN_TAG, CHINESE_TAG, JAPANESE_TAG));
				review.addTags(List.of(JAPANESE_TAG));

				//then
				List<ReviewTag> reviewTags = review.getReviewTags();
				List<Tag> tags = reviewTags.stream().map(ReviewTag::getTag).toList();

				assertThat(reviewTags).hasSize(3);
				assertThat(tags).contains(KOREAN_TAG, CHINESE_TAG, JAPANESE_TAG);
			}

		}

		@Nested
		@DisplayName("removeTags 메서드")
		class RemoveTags {

			@BeforeEach
			void givenReviewTags() {
				List<ReviewTag> reviewTags = review.getReviewTags();
				reviewTags.add(ReviewTag.mapTagToReview(review, KOREAN_TAG));
				reviewTags.add(ReviewTag.mapTagToReview(review, CHINESE_TAG));
				reviewTags.add(ReviewTag.mapTagToReview(review, JAPANESE_TAG));
			}

			@Test
			@DisplayName("아바타가 갖고 있는 태그가 주어지면 정상적으로 AvatarTag 가 제거된다.")
			void givenValidTags() {
				//when
				review.removeTags(List.of(KOREAN_TAG, CHINESE_TAG));

				//then
				List<ReviewTag> reviewTags = review.getReviewTags();
				assertThat(reviewTags).hasSize(1);
			}

		}

	}

}

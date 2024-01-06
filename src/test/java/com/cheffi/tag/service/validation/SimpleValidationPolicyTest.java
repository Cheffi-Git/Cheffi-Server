package com.cheffi.tag.service.validation;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cheffi.common.config.exception.business.BusinessException;
import com.cheffi.tag.constant.TagType;
import com.cheffi.tag.domain.Tag;

@ExtendWith(MockitoExtension.class)
class SimpleValidationPolicyTest {

	@Spy
	private SimpleValidationPolicy simpleValidationPolicy;
	@Spy
	private Tag KOREAN_TAG = new Tag(TagType.FOOD, "한식");
	@Spy
	private Tag CHINESE_TAG = new Tag(TagType.FOOD, "중식");
	@Spy
	private Tag JAPANESE_TAG = new Tag(TagType.FOOD, "일식");
	@Spy
	private Tag SPICY_TAG = new Tag(TagType.TASTE, "매콤한");
	@Spy
	private Tag SWEET_TAG = new Tag(TagType.TASTE, "달달한");
	@Spy
	private Tag TRENDY_TAG = new Tag(TagType.TASTE, "트렌디한");

	private List<Tag> REQUESTED_TAG_LIST;

	@Nested
	@DisplayName("validateTagsOfAvatar 메서드")
	class ValidateTagsOfAvatar {

		@BeforeEach
		void setUp() {
			//given
			lenient().when(KOREAN_TAG.getId()).thenReturn(1L);
			lenient().when(CHINESE_TAG.getId()).thenReturn(2L);
			lenient().when(JAPANESE_TAG.getId()).thenReturn(3L);
			lenient().when(SPICY_TAG.getId()).thenReturn(4L);
			lenient().when(SWEET_TAG.getId()).thenReturn(5L);
			lenient().when(TRENDY_TAG.getId()).thenReturn(7L);

			REQUESTED_TAG_LIST = List.of(KOREAN_TAG, CHINESE_TAG, SPICY_TAG, SWEET_TAG, TRENDY_TAG);
		}

		private static Stream<Arguments> provideTagIdLists() {
			return Stream.of(
				Arguments.of(List.of(1L, 2L), List.of(4L, 5L, 7L), "success", "유효한 요청에는 성공한다."),
				Arguments.of(List.of(1L, 2L, 3L), List.of(4L, 5L, 7L), "fail", "태그 리스트에 요청된 식별자가 존재하지 않으면 실패한다."),
				Arguments.of(List.of(1L, 4L), List.of(2L, 5L, 7L), "fail", "태그 리스트에 요청된 식별자가 존재하더라도, 타입이 맞지 않으면 실패한다.")
			);
		}

		@ParameterizedTest(name = "{index}: {3}")
		@MethodSource("provideTagIdLists")
		void parameterizedTest(List<Long> foodTagIds, List<Long> tasteTagIds, String flag, String message) {
			doNothing()
				.when(simpleValidationPolicy)
				.checkAvatarSize(REQUESTED_TAG_LIST, foodTagIds, tasteTagIds);

			if (flag.equals("fail"))
				assertThatExceptionOfType(BusinessException.class).isThrownBy(
					//when
					() -> simpleValidationPolicy.validateTagsOfAvatar(REQUESTED_TAG_LIST, foodTagIds, tasteTagIds)
				);
			else
				//then
				assertThatNoException().isThrownBy(
					//when
					() -> simpleValidationPolicy.validateTagsOfAvatar(REQUESTED_TAG_LIST, foodTagIds, tasteTagIds)
				);
		}

	}

	@Nested
	@DisplayName("validateTagsOfReview 메서드")
	class ValidateTagsOfReview {

		@BeforeEach
		void setUp() {
			//given
			lenient().when(KOREAN_TAG.getId()).thenReturn(1L);
			lenient().when(SPICY_TAG.getId()).thenReturn(4L);
			lenient().when(TRENDY_TAG.getId()).thenReturn(7L);

			REQUESTED_TAG_LIST = List.of(KOREAN_TAG, SPICY_TAG, TRENDY_TAG);
		}

		private static Stream<Arguments> provideTagIdLists() {
			return Stream.of(
				Arguments.of(List.of(1L), List.of(4L, 7L), "success", "유효한 요청에는 성공한다."),
				Arguments.of(List.of(1L, 2L), List.of(4L, 7L), "fail", "태그 리스트에 요청된 식별자가 존재하지 않으면 실패한다."),
				Arguments.of(List.of(1L, 4L), List.of(7L), "fail", "태그 리스트에 요청된 식별자가 존재하더라도, 타입이 맞지 않으면 실패한다.")
			);
		}

		@ParameterizedTest(name = "{index}: {3}")
		@MethodSource("provideTagIdLists")
		void parameterizedTest(List<Long> foodTagIds, List<Long> tasteTagIds, String flag, String message) {
			doNothing()
				.when(simpleValidationPolicy)
				.checkReviewSize(REQUESTED_TAG_LIST, foodTagIds, tasteTagIds);

			if (flag.equals("fail"))
				assertThatExceptionOfType(BusinessException.class).isThrownBy(
					//when
					() -> simpleValidationPolicy.validateTagsOfReview(REQUESTED_TAG_LIST, foodTagIds, tasteTagIds)
				);
			else
				//then
				assertThatNoException().isThrownBy(
					//when
					() -> simpleValidationPolicy.validateTagsOfReview(REQUESTED_TAG_LIST, foodTagIds, tasteTagIds)
				);
		}

	}

}

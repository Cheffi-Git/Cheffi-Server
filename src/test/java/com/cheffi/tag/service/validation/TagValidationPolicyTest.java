package com.cheffi.tag.service.validation;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cheffi.common.config.exception.business.BusinessException;
import com.cheffi.tag.domain.Tag;

@ExtendWith(MockitoExtension.class)
class TagValidationPolicyTest {

	@Spy
	private TagValidationPolicy tagValidationPolicy;

	@Nested
	@DisplayName("checkAvatarSize 메서드")
	class CheckAvatarSize {
		@Mock
		private List<Long> foodTagIds;

		@Mock
		private List<Long> tasteTagIds;

		@Mock
		private List<Tag> queriedTags;

		@Test
		@DisplayName("유효한 요청에 성공한다.")
		void givenValidSize() {
			//given
			given(queriedTags.size()).willReturn(9);
			given(foodTagIds.size()).willReturn(3);
			given(tasteTagIds.size()).willReturn(6);

			//then
			assertThatNoException().isThrownBy(
				//when
				() -> tagValidationPolicy.checkAvatarSize(queriedTags, foodTagIds, tasteTagIds)
			);

		}

		@ParameterizedTest(name = "{index}: {3}")
		@CsvSource({
			"9, 3, 6, 쿼리한 태그의 개수와 요청받은 태그들의 개수가 다르면 실패한다.",
			"1, 6, 7, 쿼리한 태그의 개수와 요청받은 태그들의 개수가 같아도 각 최소 사이즈 기준에 미달시 실패한다. - 1",
			"3, 4, 7, 쿼리한 태그의 개수와 요청받은 태그들의 개수가 같아도 각 최소 사이즈 기준에 미달시 실패한다. - 2",
		})
		@DisplayName("유효하지 않은 매개변수로 인한 검증 실패")
		void givenInvalidParameter(int foodTagListSize, int tasteTagListSize, int totalTagListSize, String message) {
			//given
			given(foodTagIds.size()).willReturn(foodTagListSize);
			given(tasteTagIds.size()).willReturn(tasteTagListSize);
			given(queriedTags.size()).willReturn(totalTagListSize);

			//then
			assertThatExceptionOfType(BusinessException.class)
				.isThrownBy(
					//when
					() -> tagValidationPolicy.checkAvatarSize(queriedTags, foodTagIds, tasteTagIds)
				);
		}

	}

}

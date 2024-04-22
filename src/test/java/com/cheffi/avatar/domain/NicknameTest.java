package com.cheffi.avatar.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cheffi.common.config.exception.business.BusinessException;

@ExtendWith(MockitoExtension.class)
class NicknameTest {

	private Nickname nickname;

	@BeforeEach
	void setUp() {
		nickname = Nickname.getRandom();
	}

	@Nested
	@DisplayName("updateOf 메서드")
	class UpdateOf {

		private static final String NULL_STRING = null;
		private static final String BLANK_STRING = "";
		private static final String LENGTH_OVER8 = RandomStringUtils.random(10);
		private static final String VALID_NICKNAME = RandomStringUtils.random(6);

		@Test
		@DisplayName("Null인 문자열이 주어지면 예외를 던진다.")
		void givenNullNickname() {
			assertThrows(BusinessException.class,
				() -> nickname.updateOf(NULL_STRING));
		}

		@Test
		@DisplayName("빈 문자열인 닉네임이 주어지면 예외를 던진다.")
		void givenBlankNickname() {
			assertThrows(BusinessException.class,
				() -> nickname.updateOf(BLANK_STRING));
		}

		@Test
		@DisplayName("8자 초과 닉네임이 주어지면 예외를 던진다.")
		void givenTooLongNickname() {
			assertThrows(BusinessException.class,
				() -> nickname.updateOf(LENGTH_OVER8));
		}

		@Test
		@DisplayName("규칙에 부합하는 닉네임이 주어지면 변경에 성공한다.")
		void givenValidNickname() {
			Nickname updatedNickname = nickname.updateOf(VALID_NICKNAME);
			assertThat(updatedNickname.getValue()).isEqualTo(VALID_NICKNAME);
			assertThat(updatedNickname.getLastUpdatedDate()).isEqualTo(LocalDate.now());
			assertThat(updatedNickname.isChangeable()).isFalse();
		}

		@Test
		@DisplayName("랜덤 닉네임은 바로 변경이 가능하다.")
		void givenRandomNickname() {
			assertThat(nickname.isChangeable()).isTrue();
		}

		@Test
		@DisplayName("닉네임 변경 후 곧 바로 재시도 시 에러를 던진다.")
		void givenValidNicknameWhenRetryRightAfterUpdateNickname() {
			Nickname updatedNickname = nickname.updateOf(VALID_NICKNAME);
			assertThat(updatedNickname.isChangeable()).isFalse();
			assertThrows(BusinessException.class,
				() -> updatedNickname.updateOf(VALID_NICKNAME));
		}

	}

}

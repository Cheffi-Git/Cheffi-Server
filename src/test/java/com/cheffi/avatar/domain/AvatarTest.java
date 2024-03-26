package com.cheffi.avatar.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.cheffi.common.config.exception.business.BusinessException;
import com.cheffi.common.constant.Address;
import com.cheffi.tag.constant.TagType;
import com.cheffi.tag.domain.Tag;
import com.cheffi.user.domain.User;

@ExtendWith(MockitoExtension.class)
class AvatarTest {

	private static final String AVATAR_NICKNAME = "Nick";
	private static final String ADDRESS_PROVINCE = "서울시";
	private static final String ADDRESS_CITY = "종로구";
	@Mock
	private User user;

	private final Address address = Address.cityAddress(ADDRESS_PROVINCE, ADDRESS_CITY);
	@Mock
	private Address childAddress;

	private Avatar avatar;

	@BeforeEach
	void setUp() {
		avatar = new Avatar(AVATAR_NICKNAME, user);
	}

	@Nested
	@DisplayName("changeAddress 메서드")
	class ChangeAddress {
		@Test
		@DisplayName("주소를 단순한 주소(Address)를 주지 않으면 예외가 발생한다.")
		void givenDetailedAddress() {
			given(childAddress.isSimpleAddress()).willReturn(false);
			assertThrows(IllegalArgumentException.class, () ->
				avatar.changeAddress(childAddress)
			);
		}

		@Test
		@DisplayName("주소가 단순한 주소일 경우 변경에 성공한다.")
		void givenSimpleAddress() {
			assertDoesNotThrow(() -> avatar.changeAddress(address));
			assertThat(avatar.getAddress()).isEqualTo(address);
		}
	}

	@Nested
	@DisplayName("changeNickname 메서드")
	class ChangeNickname {

		private static final String NULL_STRING = null;
		private static final String BLANK_STRING = "";
		private static final String LENGTH_OVER8 = RandomStringUtils.random(10);
		private static final String VALID_NICKNAME = RandomStringUtils.random(6);

		@Test
		@DisplayName("Null인 닉네임이 주어지면 예외를 던진다.")
		void givenNullNickname() {
			assertThrows(BusinessException.class,
				() -> avatar.changeNickname(NULL_STRING));
		}

		@Test
		@DisplayName("빈 문자열인 닉네임이 주어지면 예외를 던진다.")
		void givenBlankNickname() {
			assertThrows(BusinessException.class,
				() -> avatar.changeNickname(BLANK_STRING));
		}

		@Test
		@DisplayName("8자 초과 닉네임이 주어지면 예외를 던진다.")
		void givenTooLongNickname() {
			assertThrows(BusinessException.class,
				() -> avatar.changeNickname(LENGTH_OVER8));
		}

		@Test
		@DisplayName("규칙에 부합하는 닉네임이 주어지면 변경에 성공한다.")
		void givenValidNickname() {
			avatar.changeNickname(VALID_NICKNAME);
			assertThat(avatar.getNickname()).isEqualTo(VALID_NICKNAME);
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
			@DisplayName("중복된 태그 요청에 대해 AvatarTag 는 한 번만 추가된다.")
			void givenDuplicated() {
				//when
				avatar.addTags(TAG_LIST);
				avatar.addTags(List.of(KOREAN_TAG, CHINESE_TAG));
				avatar.addTags(List.of(KOREAN_TAG, CHINESE_TAG, JAPANESE_TAG));
				avatar.addTags(List.of(JAPANESE_TAG));
				//then
				List<AvatarTag> avatarTags = avatar.getAvatarTags();
				List<Tag> tags = avatarTags.stream().map(AvatarTag::getTag).toList();
				assertThat(avatarTags).hasSize(3);
				assertThat(tags).contains(KOREAN_TAG, CHINESE_TAG, JAPANESE_TAG);
			}

		}

		@Nested
		@DisplayName("removeTags 메서드")
		class RemoveTags {
			@BeforeEach
			void given() {
				avatar.getAvatarTags().add(AvatarTag.mapTagToAvatar(avatar, KOREAN_TAG));
				avatar.getAvatarTags().add(AvatarTag.mapTagToAvatar(avatar, CHINESE_TAG));
				avatar.getAvatarTags().add(AvatarTag.mapTagToAvatar(avatar, JAPANESE_TAG));
			}

			@Test
			@DisplayName("아바타가 갖고 있는 태그가 주어지면 정상적으로 AvatarTag 가 제거된다.")
			void givenValidTags() {
				//when
				avatar.removeTags(List.of(KOREAN_TAG, CHINESE_TAG));

				//then
				List<AvatarTag> avatarTags = avatar.getAvatarTags();
				assertThat(avatarTags).hasSize(1);
			}
		}

		@Nested
		@DisplayName("hasTags 메서드")
		class HasTags {

			@Test
			@DisplayName("아바타가 갖고 있는 태그가 있으면 true 를 반환한다.")
			void givenTagsThatAvatarHas() {
				//given
				avatar.getAvatarTags().add(AvatarTag.mapTagToAvatar(avatar, KOREAN_TAG));

				//when
				boolean hasTags = avatar.hasTags();

				//then
				assertThat(hasTags).isTrue();
			}

			@Test
			@DisplayName("아바타가 갖고 있는 태그가 없으면 false 를 반환한다.")
			void givenTagsThatAvatarDoesNotHave() {
				//given
				ReflectionTestUtils.setField(avatar, "avatarTags", Collections.emptyList());

				//when
				boolean hasTags = avatar.hasTags();

				//then
				assertThat(hasTags).isFalse();
			}

		}

	}

}

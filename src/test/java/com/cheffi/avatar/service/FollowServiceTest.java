package com.cheffi.avatar.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import com.cheffi.avatar.domain.Avatar;
import com.cheffi.avatar.domain.Follow;
import com.cheffi.avatar.dto.response.AddFollowResponse;
import com.cheffi.avatar.dto.response.UnfollowResponse;
import com.cheffi.avatar.repository.AvatarJpaRepository;
import com.cheffi.avatar.repository.AvatarRepository;
import com.cheffi.avatar.repository.FollowJpaRepository;
import com.cheffi.avatar.repository.FollowRepository;
import com.cheffi.common.config.exception.business.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class FollowServiceTest {

	@Mock
	private AvatarRepository avatarRepository;
	@Mock
	private FollowRepository followRepository;
	@Mock
	private ProfilePhotoService profilePhotoService;
	@Mock
	private FollowJpaRepository followJpaRepository;
	@Mock
	private AvatarJpaRepository avatarJpaRepository;
	@Mock
	private ApplicationEventPublisher eventPublisher;

	private AvatarService avatarService;

	private FollowService followService;

	@Mock
	private Avatar follower;
	@Mock
	private Avatar followee;
	@Mock
	private Follow follow;

	private static final long FOLLOWER_ID = 1L;
	private static final long FOLLOWEE_ID = 2L;

	@BeforeEach
	void setUp() {
		avatarService = new AvatarService(avatarRepository, avatarJpaRepository, profilePhotoService);
		followService = new FollowService(followRepository, followJpaRepository, avatarService, eventPublisher);
	}

	@Nested
	@DisplayName("addFollow 메서드")
	class AddFollow {

		@Test
		@DisplayName("success - 팔로우 등록")
		void successAddFollow() {

			AddFollowResponse addFollowResponse = new AddFollowResponse(FOLLOWER_ID, FOLLOWEE_ID);

			try (MockedStatic<Follow> staticFollow = Mockito.mockStatic(Follow.class);
				 MockedStatic<AddFollowResponse> staticAddFollowResponse = Mockito.mockStatic(
					 AddFollowResponse.class)) {
				when(avatarRepository.findById(FOLLOWER_ID)).thenReturn(Optional.of(follower));
				when(avatarRepository.findById(FOLLOWEE_ID)).thenReturn(Optional.of(followee));
				when(followRepository
					.existsBySubjectAndTarget(FOLLOWER_ID, FOLLOWEE_ID))
					.thenReturn(false);
				staticFollow
					.when(() -> Follow.createFollowRelationship(follower, followee))
					.thenReturn(follow);
				staticAddFollowResponse
					.when(() -> AddFollowResponse.from(follow))
					.thenReturn(addFollowResponse);
				when(followRepository.save(follow)).thenReturn(follow);

				AddFollowResponse response = followService.addFollow(FOLLOWER_ID, FOLLOWEE_ID);

				assertEquals(FOLLOWER_ID, response.followerId());
				assertEquals(FOLLOWEE_ID, response.followeeId());
			}

		}

		@Test
		@DisplayName("fail - 존재하지 않는 아바타에 대한 팔로우 시도")
		void failAddFollow_AVATAR_NOT_EXISTS() {

			when(avatarRepository.findById(FOLLOWER_ID)).thenReturn(Optional.of(follower));
			when(avatarRepository.findById(FOLLOWEE_ID)).thenReturn(Optional.empty());

			assertThrows(EntityNotFoundException.class, () -> followService.addFollow(FOLLOWER_ID, FOLLOWEE_ID));
		}

		@Test
		@DisplayName("fail - 이미 팔로우 중인 아바타에 대한 팔로우 시도")
		void failAddFollow_ALREADY_FOLLOWED() {

			when(avatarRepository.findById(FOLLOWER_ID)).thenReturn(Optional.of(follower));
			when(avatarRepository.findById(FOLLOWEE_ID)).thenReturn(Optional.of(followee));
			when(followRepository.existsBySubjectAndTarget(FOLLOWER_ID, FOLLOWEE_ID))
				.thenReturn(true);

			assertThrows(RuntimeException.class, () -> followService.addFollow(FOLLOWER_ID, FOLLOWEE_ID));
		}

	}

	@Nested
	@DisplayName("unFollow 메서드")
	class Unfollow {

		@Test
		@DisplayName("success - 팔로우 취소")
		void successUnFollow() {

			when(avatarRepository.findById(FOLLOWER_ID)).thenReturn(Optional.of(follower));
			when(avatarRepository.findById(FOLLOWEE_ID)).thenReturn(Optional.of(followee));
			when(followRepository
				.findBySubjectAndTarget(follower, followee))
				.thenReturn(Optional.of(follow));
			doNothing().when(followRepository).delete(follow);

			UnfollowResponse response = followService.unfollow(FOLLOWER_ID, FOLLOWEE_ID);

			assertEquals(FOLLOWER_ID, response.followerId());
			assertEquals(FOLLOWEE_ID, response.followeeId());
		}

		@Test
		@DisplayName("fail - 존재하지 않는 아바타에대한 팔로우 시도")
		void failAddFollow_AVATAR_NOT_EXISTS() {

			when(avatarRepository.findById(FOLLOWER_ID)).thenReturn(Optional.of(follower));
			when(avatarRepository.findById(FOLLOWEE_ID)).thenReturn(Optional.empty());

			assertThrows(EntityNotFoundException.class, () -> followService.unfollow(FOLLOWER_ID, FOLLOWEE_ID));
		}

		@Test
		@DisplayName("fail - 팔로우 상태가 아닌 아바타에 대한 언팔로우 시도")
		void failAddFollow_ALREADY_FOLLOWED() {

			when(avatarRepository.findById(FOLLOWER_ID)).thenReturn(Optional.of(follower));
			when(avatarRepository.findById(FOLLOWEE_ID)).thenReturn(Optional.of(followee));
			when(followRepository.findBySubjectAndTarget(follower, followee))
				.thenReturn(Optional.empty());

			assertThrows(RuntimeException.class, () -> followService.unfollow(FOLLOWER_ID, FOLLOWEE_ID));
		}

	}

}

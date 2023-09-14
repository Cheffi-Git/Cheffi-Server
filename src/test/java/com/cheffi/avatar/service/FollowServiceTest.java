package com.cheffi.avatar.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cheffi.avatar.domain.Avatar;
import com.cheffi.avatar.domain.Follow;
import com.cheffi.avatar.dto.response.AddFollowResponse;
import com.cheffi.avatar.dto.response.UnfollowResponse;
import com.cheffi.avatar.repository.AvatarRepository;
import com.cheffi.avatar.repository.FollowRepository;
import com.cheffi.common.config.exception.business.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class FollowServiceTest {


	@Mock
	private AvatarRepository avatarRepository;
	@Mock
	private FollowRepository followRepository;

	@InjectMocks
	private FollowService followService;

	@Mock
	private Avatar follower;
	@Mock
	private Avatar followee;
	@Mock
	private Follow follow;

	private static long FOLLOWER_ID = 1L;
	private static long FOLLOWEE_ID = 2L;

	@Nested
	@DisplayName("addFollow 메서드")
	class AddFollow{

		@Test
		@DisplayName("성공 - 팔로우 등록")
		void successAddFollow() {

			MockedStatic<Follow> staticFollow = Mockito.mockStatic(Follow.class);
			MockedStatic<AddFollowResponse> staticAddFollowResponse = Mockito.mockStatic(AddFollowResponse.class);
			AddFollowResponse addFollowResponse = new AddFollowResponse(FOLLOWER_ID, FOLLOWEE_ID);

			try {
				when(avatarRepository.findById(FOLLOWER_ID)).thenReturn(Optional.of(follower));
				when(avatarRepository.findById(FOLLOWEE_ID)).thenReturn(Optional.of(followee));
				when(followRepository
					.existsBySubjectAndTarget(followee, follower))
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
			} finally {
				staticFollow.close();
				staticAddFollowResponse.close();
			}

		}

		@Test
		@DisplayName("실패 - 존재하지 않는 아바타에 대한 팔로우 시도")
		void failAddFollow_AVATAR_NOT_EXISTS() {

			when(avatarRepository.findById(FOLLOWER_ID)).thenReturn(Optional.of(follower));
			when(avatarRepository.findById(FOLLOWEE_ID)).thenReturn(Optional.empty());

			assertThrows(EntityNotFoundException.class, () -> {
				followService.addFollow(FOLLOWER_ID, FOLLOWEE_ID);
			});
		}

		@Test
		@DisplayName("실패 - 이미 팔로우 중인 아바타에 대한 팔로우 시도")
		void failAddFollow_ALREADY_FOLLOWED() {

			when(avatarRepository.findById(FOLLOWER_ID)).thenReturn(Optional.of(follower));
			when(avatarRepository.findById(FOLLOWEE_ID)).thenReturn(Optional.of(followee));
			when(followRepository.existsBySubjectAndTarget(follower, followee))
				.thenReturn(true);

			assertThrows(RuntimeException.class, () -> {
				followService.addFollow(FOLLOWER_ID, FOLLOWEE_ID);
			});
		}

	}

	@Nested
	@DisplayName("unFollow 메서드")
	class Unfollow{

		@Test
		@DisplayName("성공 - 팔로우 취소")
		void successUnFollow() {

			MockedStatic<UnfollowResponse> staticUnFollowResponse = Mockito.mockStatic(UnfollowResponse.class);
			UnfollowResponse unfollowResponse = new UnfollowResponse(FOLLOWER_ID, FOLLOWEE_ID);

			try {
				when(avatarRepository.findById(FOLLOWER_ID)).thenReturn(Optional.of(follower));
				when(avatarRepository.findById(FOLLOWEE_ID)).thenReturn(Optional.of(followee));
				when(followRepository
					.findBySubjectAndTarget(follower, followee))
					.thenReturn(Optional.of(follow));
				doNothing().when(followRepository).delete(follow);
				staticUnFollowResponse
					.when(() -> UnfollowResponse.from(follow))
					.thenReturn(unfollowResponse);

				UnfollowResponse response = followService.unfollow(FOLLOWER_ID, FOLLOWEE_ID);

				assertEquals(FOLLOWER_ID, response.followerId());
				assertEquals(FOLLOWEE_ID, response.followeeId());
			} finally {
				staticUnFollowResponse.close();
			}
		}

		@Test
		@DisplayName("실패 - 존재하지 않는 아바타에대한 팔로우 시도")
		void failAddFollow_AVATAR_NOT_EXISTS() {

			when(avatarRepository.findById(FOLLOWER_ID)).thenReturn(Optional.of(follower));
			when(avatarRepository.findById(FOLLOWEE_ID)).thenReturn(Optional.empty());

			assertThrows(EntityNotFoundException.class, () -> {
				followService.unfollow(FOLLOWER_ID, FOLLOWEE_ID);
			});
		}

		@Test
		@DisplayName("실패 - 팔로우 상태가 아닌 아바타에 대한 언팔로우 시도")
		void failAddFollow_ALREADY_FOLLOWED() {

			when(avatarRepository.findById(FOLLOWER_ID)).thenReturn(Optional.of(follower));
			when(avatarRepository.findById(FOLLOWEE_ID)).thenReturn(Optional.of(followee));
			when(followRepository.findBySubjectAndTarget(follower, followee))
				.thenReturn(Optional.empty());

			assertThrows(RuntimeException.class, () -> {
				followService.unfollow(FOLLOWER_ID, FOLLOWEE_ID);
			});
		}

	}

}
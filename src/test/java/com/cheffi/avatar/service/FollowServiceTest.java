package com.cheffi.avatar.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
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
import com.cheffi.avatar.dto.response.GetMyFolloweeData;
import com.cheffi.avatar.dto.response.UnfollowResponse;
import com.cheffi.avatar.repository.AvatarRepository;
import com.cheffi.avatar.repository.FollowRepository;
import com.cheffi.common.config.exception.business.EntityNotFoundException;
import com.cheffi.common.request.CursorPageable;
import com.cheffi.common.response.CursorPageResponse;

@ExtendWith(MockitoExtension.class)
class FollowServiceTest {


	@Mock
	private AvatarRepository avatarRepository;
	@Mock
	private FollowRepository followRepository;
	@Mock
	private ProfilePhotoService profilePhotoService;
	@InjectMocks
	private FollowService followService;

	@Mock
	private Avatar follower;
	@Mock
	private Avatar followee;
	@Mock
	private Follow follow;
	@Mock
	CursorPageable cursorPageable;
	@Mock
	List<GetMyFolloweeData> getFolloweeData;
	@Mock
	CursorPageResponse<List<GetMyFolloweeData>> cursorPageResponse;
	@Mock
	private List<Follow> follows;

	private static final long FOLLOWER_ID = 1L;
	private static final long FOLLOWEE_ID = 2L;
	private static final long LAST_FOLLOW_ID = 10L;
	private static final Long INVALID_FOLLOW_ID = -1L;

	@BeforeEach
	void setUp() {

		AvatarService avatarService = new AvatarService(avatarRepository, profilePhotoService);
		followService = new FollowService(followRepository, avatarService);
	}

	@Nested
	@DisplayName("getFollowee 메서드")
	class getFollowee {

		@Test
		@DisplayName("success - 팔로우중인 회원 1명이상 조회되었으며 커서의 다음 값이 존재하는 경우")
		void successGetFollowee_case1() {
			try (MockedStatic<CursorPageResponse> staticCursorPageResponse = Mockito.mockStatic(CursorPageResponse.class);
				 MockedStatic<GetMyFolloweeData> staticGetFollowData = Mockito.mockStatic(GetMyFolloweeData.class)) {

				final int FOLLOWS_SIZE = 2;
				final String ENCODED_CURSOR_VALUE = "ENCODED STRING";

				when(avatarRepository.existsById(FOLLOWER_ID))
					.thenReturn(true);
				when(followRepository.getFollows(FOLLOWER_ID, cursorPageable))
					.thenReturn(follows);
				when(follows.get(follows.size() - 1))
					.thenReturn(follow);
				when(follow.getId())
					.thenReturn(LAST_FOLLOW_ID);
				when(followRepository.existsFollowsAfterId(LAST_FOLLOW_ID))
					.thenReturn(true);
				when(cursorPageable.getEncodedCursor(LAST_FOLLOW_ID, true))
					.thenReturn(ENCODED_CURSOR_VALUE);

				staticGetFollowData
					.when(() -> GetMyFolloweeData.mapFollowsToData(follows))
					.thenReturn(getFolloweeData);
				staticCursorPageResponse
					.when(() -> CursorPageResponse.success(getFolloweeData, ENCODED_CURSOR_VALUE))
					.thenReturn(cursorPageResponse);

				when(cursorPageResponse.getNextPageCursor())
					.thenReturn(ENCODED_CURSOR_VALUE);
				when(cursorPageResponse.getData())
					.thenReturn(getFolloweeData);
				when(getFolloweeData.size())
					.thenReturn(FOLLOWS_SIZE);

				CursorPageResponse<List<GetMyFolloweeData>> response =
					followService.getFollowee(FOLLOWER_ID, cursorPageable);

				assertNotNull(response);
				assertEquals(FOLLOWS_SIZE, response.getData().size());
				assertEquals(ENCODED_CURSOR_VALUE, response.getNextPageCursor());

			}

		}

		@Test
		@DisplayName("success - 팔로우중인 회원 1명이상 조회되었으며 커서의 다음 값이 존재하지 않는 경우")
		void successGetFollowee_case2() {
			try (MockedStatic<CursorPageResponse> staticCursorPageResponse = Mockito.mockStatic(CursorPageResponse.class);
				 MockedStatic<GetMyFolloweeData> staticGetFollowResponse = Mockito.mockStatic(GetMyFolloweeData.class)) {

				final int FOLLOWS_SIZE = 2;
				final String RETURN_CURSOR_VALUE = "";

				when(avatarRepository.existsById(FOLLOWER_ID))
					.thenReturn(true);
				when(followRepository.getFollows(FOLLOWER_ID, cursorPageable))
					.thenReturn(follows);
				when(follows.get(follows.size() - 1))
					.thenReturn(follow);
				when(follow.getId())
					.thenReturn(LAST_FOLLOW_ID);
				when(followRepository.existsFollowsAfterId(LAST_FOLLOW_ID))
					.thenReturn(false);
				when(cursorPageable.getEncodedCursor(LAST_FOLLOW_ID, false))
					.thenReturn(RETURN_CURSOR_VALUE);

				staticGetFollowResponse
					.when(() -> GetMyFolloweeData.mapFollowsToData(follows))
					.thenReturn(getFolloweeData);
				staticCursorPageResponse
					.when(() -> CursorPageResponse.success(getFolloweeData, RETURN_CURSOR_VALUE))
					.thenReturn(cursorPageResponse);

				when(cursorPageResponse.getNextPageCursor())
					.thenReturn(RETURN_CURSOR_VALUE);
				when(cursorPageResponse.getData())
					.thenReturn(getFolloweeData);
				when(getFolloweeData.size())
					.thenReturn(FOLLOWS_SIZE);

				CursorPageResponse<List<GetMyFolloweeData>> response =
					followService.getFollowee(FOLLOWER_ID, cursorPageable);

				assertNotNull(response);
				assertEquals(FOLLOWS_SIZE, response.getData().size());
				assertEquals("", response.getNextPageCursor());

			}

		}

		@Test
		@DisplayName("success - 팔로우중인 회원 0명인 경우")
		void successGetFollowee_case3() {

			try (MockedStatic<CursorPageResponse> staticCursorPageResponse = Mockito.mockStatic(CursorPageResponse.class);
				 MockedStatic<GetMyFolloweeData> staticGetFollowResponse = Mockito.mockStatic(GetMyFolloweeData.class)) {

				final int FOLLOWS_SIZE = 0;
				final String RETURN_CURSOR_VALUE = "";

				when(avatarRepository.existsById(FOLLOWER_ID))
					.thenReturn(true);
				when(followRepository.getFollows(FOLLOWER_ID, cursorPageable))
					.thenReturn(follows);
				when(follows.isEmpty())
					.thenReturn(true);

				staticGetFollowResponse
					.when(() -> GetMyFolloweeData.mapFollowsToData(follows))
					.thenReturn(getFolloweeData);
				staticCursorPageResponse
					.when(() -> CursorPageResponse.success(getFolloweeData, RETURN_CURSOR_VALUE))
					.thenReturn(cursorPageResponse);

				when(cursorPageResponse.getNextPageCursor())
					.thenReturn(RETURN_CURSOR_VALUE);
				when(cursorPageResponse.getData())
					.thenReturn(getFolloweeData);
				when(getFolloweeData.size())
					.thenReturn(FOLLOWS_SIZE);

				CursorPageResponse<List<GetMyFolloweeData>> response =
					followService.getFollowee(FOLLOWER_ID, cursorPageable);

				assertNotNull(response);
				assertEquals(FOLLOWS_SIZE, response.getData().size());
				assertEquals("", response.getNextPageCursor());

			}
		}

		@Test
		@DisplayName("fail - 존재하지 않는 아바타로 팔로잉 목록 조회 시도")
		void fail_getFollowee_AVATAR_NOT_EXISTS() {

			when(avatarRepository.findById(FOLLOWER_ID)).thenReturn(Optional.empty());

			assertThrows(EntityNotFoundException.class, () -> {
				followService.addFollow(FOLLOWER_ID, FOLLOWEE_ID);
			});
		}
	}


	@Nested
	@DisplayName("addFollow 메서드")
	class AddFollow{

		@Test
		@DisplayName("success - 팔로우 등록")
		void successAddFollow() {

			AddFollowResponse addFollowResponse = new AddFollowResponse(FOLLOWER_ID, FOLLOWEE_ID);

			try (MockedStatic<Follow> staticFollow = Mockito.mockStatic(Follow.class);
				 MockedStatic<AddFollowResponse> staticAddFollowResponse = Mockito.mockStatic(AddFollowResponse.class)) {
				when(avatarRepository.findById(FOLLOWER_ID)).thenReturn(Optional.of(follower));
				when(avatarRepository.findById(FOLLOWEE_ID)).thenReturn(Optional.of(followee));
				when(followRepository
					.existsBySubjectAndTarget(followee, follower))
					.thenReturn(false);
				staticFollow
					.when(() -> Follow.createFollowRelationship(follower, followee))
					.thenReturn(follow);
				staticAddFollowResponse
					.when(() -> AddFollowResponse.of(follow))
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

			assertThrows(EntityNotFoundException.class, () -> {
				followService.addFollow(FOLLOWER_ID, FOLLOWEE_ID);
			});
		}

		@Test
		@DisplayName("fail - 이미 팔로우 중인 아바타에 대한 팔로우 시도")
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

			assertThrows(EntityNotFoundException.class, () -> {
				followService.unfollow(FOLLOWER_ID, FOLLOWEE_ID);
			});
		}

		@Test
		@DisplayName("fail - 팔로우 상태가 아닌 아바타에 대한 언팔로우 시도")
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
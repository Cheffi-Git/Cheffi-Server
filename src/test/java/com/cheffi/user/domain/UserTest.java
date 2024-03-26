package com.cheffi.user.domain;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cheffi.user.constant.RoleType;
import com.cheffi.user.constant.UserType;
import com.cheffi.user.dto.UserCreateRequest;

@ExtendWith(MockitoExtension.class)
class UserTest {

	private User user;
	private static final Role ROLE_ADMIN = new Role(RoleType.ADMIN);
	private static final Role ROLE_USER = new Role(RoleType.USER);
	private static final Role ROLE_GUEST = new Role(RoleType.GUEST);
	private static final Role NO_PROFILE = new Role(RoleType.NO_PROFILE);
	private static final List<Role> ROLES = List.of(ROLE_USER, ROLE_GUEST);
	private static final String USER_NAME = "홍길동";
	private static final String USER_EMAIL = "foo@naver.com";
	private static final UserType USER_TYPE = UserType.KAKAO;

	@BeforeEach
	void setUp() {
		user = User.builder()
			.name(USER_NAME)
			.email(USER_EMAIL)
			.activated(true)
			.withdrawn(false)
			.expired(false)
			.locked(false)
			.userType(USER_TYPE)
			.build();
	}

	@Nested
	@DisplayName("addRoles 메서드")
	class AddRoles {

		@Test
		@DisplayName("Role 추가가 정상적으로 반영된다.")
		void givenRolesWhenAddRolesThenApplied() {
			//when
			user.addRoles(List.of(ROLE_ADMIN, ROLE_USER));

			//then
			List<UserRole> userRoles = user.getUserRoles();
			List<Role> roles = userRoles.stream().map(UserRole::getRole).collect(Collectors.toList());
			assertThat(userRoles).hasSize(2);
			assertThat(roles).contains(ROLE_ADMIN, ROLE_USER);
		}

		@Test
		@DisplayName("중복된 ROLE Entity 는 매핑되지 않는다.")
		void givenDuplicatedRolesWhenAddRolesThenUnique() {
			//when
			user.addRoles(List.of(ROLE_ADMIN, ROLE_USER));
			user.addRoles(List.of(ROLE_ADMIN, ROLE_USER, ROLE_GUEST));
			user.addRoles(List.of(ROLE_ADMIN));
			user.addRoles(List.of(ROLE_ADMIN, ROLE_GUEST));

			//then
			List<UserRole> userRoles = user.getUserRoles();
			List<Role> roles = userRoles.stream().map(UserRole::getRole).collect(Collectors.toList());
			assertThat(userRoles).hasSize(3);
			assertThat(roles).contains(ROLE_ADMIN, ROLE_USER, ROLE_GUEST);
		}

	}

	@Nested
	@DisplayName("removeRoles 메서드")
	class RemoveRoles {

		@Test
		@DisplayName("주어진 Role 을 유저가 갖고 있으면 Role 정상적으로 삭제된다.")
		void givenRolesThatUserHasThenRemoved() {
			//given
			user.getUserRoles().add(UserRole.mapRoleToUser(user, NO_PROFILE));
			user.getUserRoles().add(UserRole.mapRoleToUser(user, ROLE_GUEST));
			user.getUserRoles().add(UserRole.mapRoleToUser(user, ROLE_USER));

			//when
			user.removeRole(NO_PROFILE);

			//then
			List<Role> roles = user.getUserRoles().stream().map(UserRole::getRole).toList();
			assertThat(roles).hasSize(2).contains(ROLE_USER);
		}

		@Test
		@DisplayName("주어진 Role 을 유저가 갖고 있지 않으면 상태 변화가 없다. ")
		void givenRolesThatUserDoesNotHaveThenNoChanges() {
			//given
			user.getUserRoles().add(UserRole.mapRoleToUser(user, NO_PROFILE));
			user.getUserRoles().add(UserRole.mapRoleToUser(user, ROLE_GUEST));
			user.getUserRoles().add(UserRole.mapRoleToUser(user, ROLE_USER));

			//when
			user.removeRole(ROLE_ADMIN);

			//then
			List<Role> roles = user.getUserRoles().stream().map(UserRole::getRole).collect(Collectors.toList());
			assertThat(roles).hasSize(3).contains(NO_PROFILE, ROLE_USER, ROLE_GUEST);
		}

	}

	@Nested
	@DisplayName("createUser 메서드")
	class CreateUser {

		@Spy
		private UserCreateRequest createRequest;

		@BeforeEach
		void setUp() {
			// lenient() 메서드는 스터빙이 호출되지 않아서 생기는 Unnecessary Stubbing Exception 예외를 방지해준다.
			lenient().when(createRequest.email()).thenReturn(USER_EMAIL);
			lenient().when(createRequest.name()).thenReturn(USER_NAME);
			lenient().when(createRequest.userType()).thenReturn(USER_TYPE);
			lenient().when(createRequest.roles()).thenReturn(ROLES);
		}

		@Test
		@DisplayName("패스워스가 없어도 성공한다.")
		void givenNoPasswordWhenCreateUserThenSuccess() {
			//given
			given(createRequest.password()).willReturn(null);

			//when
			User newUser = User.createUser(createRequest);

			//then
			assertThat(newUser.getEmail()).isEqualTo(USER_EMAIL);
			assertThat(newUser.getName()).isEqualTo(USER_NAME);
			assertThat(newUser.getUserType()).isEqualTo(USER_TYPE);
		}

		@Test
		@DisplayName("이메일이 빈 문자열이면 예외를 던진다")
		void givenBlankOrNullEmailWhenCreateUserThenSuccess() {
			given(createRequest.email()).willReturn("");

			assertThatThrownBy(() -> User.createUser(createRequest))
				.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		@DisplayName("이메일이 null 이면 예외를 던진다")
		void givenNullEmailWhenCreateUserThenSuccess() {
			given(createRequest.email()).willReturn(null);

			assertThatThrownBy(() -> User.createUser(createRequest))
				.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		@DisplayName("이름이 빈 문자열이면 예외를 던진다")
		void givenBlankOrNullNameWhenCreateUserThenSuccess() {
			given(createRequest.name()).willReturn("");

			assertThatThrownBy(() -> User.createUser(createRequest))
				.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		@DisplayName("이름이 null 이면 예외를 던진다")
		void givenNullNameWhenCreateUserThenSuccess() {
			given(createRequest.name()).willReturn(null);

			assertThatThrownBy(() -> User.createUser(createRequest))
				.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		@DisplayName("유저타입이 null 이면 예외를 던진다.")
		void givenNullUserTypeWhenCreateUserThenSuccess() {
			given(createRequest.userType()).willReturn(null);

			assertThatThrownBy(() -> User.createUser(createRequest))
				.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		@DisplayName("유저의 Role 리스트가 비어있이면 예외를 던진다.")
		void givenEmptyRoleWhenCreateUserThenSuccess() {
			given(createRequest.roles()).willReturn(List.of());

			assertThatThrownBy(() -> User.createUser(createRequest))
				.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		@DisplayName("유저의 Role 리스트가 null 이면 예외를 던진다.")
		void givenNullRoleWhenCreateUserThenSuccess() {
			given(createRequest.roles()).willReturn(null);

			assertThatThrownBy(() -> User.createUser(createRequest))
				.isInstanceOf(IllegalArgumentException.class);
		}

	}

	@Nested
	@DisplayName("로그인 날짜 관련 메서드")
	class LoginDate {

		private static final LocalDate TWO_DAYS_AGO = LocalDate.now().minusDays(2);
		private static final LocalDate TODAY = LocalDate.now();

		@Nested
		@DisplayName("hasLoggedInToday 메서드")
		class HasLoggedInToday {

			@Test
			@DisplayName("마지막 로그인 날짜가 오늘 이전이면 flase 를 반환한다.")
			void given2DaysAgo() {
				//when
				user.setLastLoginDate(TWO_DAYS_AGO);
				//then
				assertThat(user.hasLoggedInToday()).isFalse();
			}

			@Test
			@DisplayName("마지막 로그인 날짜가 오늘이면 true 를 반환한다.")
			void givenToday() {
				//when
				user.setLastLoginDate(TODAY);
				//then
				assertThat(user.hasLoggedInToday()).isTrue();
			}

		}

		@Nested
		@DisplayName("updateLastLoginDate 메서드")
		class UpdateLastLoginDate {

			@Test
			@DisplayName("마지막 로그인 날짜가 오늘 이전이면 오늘로 변경된다.")
			void given2DaysAgo() {
				//given
				user.setLastLoginDate(TWO_DAYS_AGO);

				//when
				user.updateLastLoginDate();

				//then
				assertThat(user.getLastLoginDate()).isEqualTo(TODAY);
			}

		}

	}
}

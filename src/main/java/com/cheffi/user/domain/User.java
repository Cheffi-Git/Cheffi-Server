package com.cheffi.user.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import com.cheffi.common.domain.BaseTimeEntity;
import com.cheffi.notification.domain.Device;
import com.cheffi.user.constant.Password;
import com.cheffi.user.constant.UserType;
import com.cheffi.user.dto.UserCreateRequest;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String email;

	@NotNull
	private boolean locked;

	@NotNull
	private boolean expired;
	@NotNull
	private boolean withdrawn;
	private LocalDateTime withdrawnDate;
	@NotNull
	private boolean activated;
	private LocalDateTime lastPwChangedDate;
	@NotNull
	private String name;

	@NotNull
	@Enumerated(EnumType.STRING)
	private UserType userType;
	@NotNull
	private boolean adAgreed;
	@NotNull
	private boolean analysisAgreed;
	@NotNull
	private boolean notificationAllowed;
	@Embedded
	private Password password;
	private LocalDate lastLoginDate;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserRole> userRoles = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Device> devices = new ArrayList<>();

	@Builder
	private User(String email, boolean locked, boolean expired, boolean withdrawn, boolean activated, String name,
		UserType userType, boolean notificationAllowed, Password password) {
		this.email = email;
		this.locked = locked;
		this.expired = expired;
		this.withdrawn = withdrawn;
		this.activated = activated;
		this.name = name;
		this.userType = userType;
		this.notificationAllowed = notificationAllowed;
		this.password = password;
	}

	public void addRoles(List<Role> roles) {
		roles.stream().filter(r ->
				userRoles.stream().map(UserRole::getRole).noneMatch(ar -> ar.equals(r)))
			.map(r -> UserRole.mapRoleToUser(this, r))
			.forEach(userRoles::add);
	}

	public void removeRole(Role role) {
		userRoles.removeIf(ur -> ur.getRole().equals(role));
	}

	/**
	 * 회원 가입 공통 메서드
	 */
	public static User createUser(UserCreateRequest request) {
		Assert.hasText(request.email(), "이메일이 입력되지 않았습니다.");
		Assert.hasText(request.name(), "이름이 입력되지 않았습니다");
		Assert.notNull(request.userType(), "유저타입은 null일 수 없습니다.");
		Assert.notEmpty(request.roles(), "유저 권한(Role)은 최소한 1개는 부여되어야 합니다.");

		User createdUser = User.builder()
			.email(request.email())
			.name(request.name())
			.userType(request.userType())
			.locked(false)
			.expired(false)
			.withdrawn(false)
			.activated(true)
			.notificationAllowed(false)
			.password(request.password())
			.build();
		createdUser.addRoles(request.roles());
		return createdUser;
	}

	public void changeTermsAgreement(boolean adAgreed, boolean analysisAgreed) {
		this.adAgreed = adAgreed;
		this.analysisAgreed = analysisAgreed;
	}

	public List<Role> getRoles() {
		return this.getUserRoles().stream().map(UserRole::getRole).toList();
	}

	public boolean hasLoggedInToday() {
		return this.lastLoginDate.equals(LocalDate.now());
	}

	public void updateLastLoginDate() {
		this.lastLoginDate = LocalDate.now();
	}

	public boolean isUserOf(String provider) {
		return UserType.from(provider).equals(userType);
	}

	/**
	 * 테스트용 메서드
	 */
	void setLastLoginDate(LocalDate lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
}

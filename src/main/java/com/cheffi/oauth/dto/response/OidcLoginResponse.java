package com.cheffi.oauth.dto.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.cheffi.avatar.domain.Avatar;
import com.cheffi.avatar.domain.ProfilePhoto;
import com.cheffi.oauth.model.AuthenticationToken;
import com.cheffi.oauth.model.UserPrincipal;
import com.cheffi.user.constant.RoleType;
import com.cheffi.user.constant.UserType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record OidcLoginResponse(
	@Schema(description = "이메일", example = "user1234@naver.com", required = true)
	String email,
	@Schema(description = "계정 잠김 여부", required = true)
	boolean locked,
	@Schema(description = "계정 만료 여부", required = true)
	boolean expired,
	@Schema(description = "계정 비활성화 여부", required = true)
	boolean activated,
	@Schema(description = "마지막 비밀번호 변경 일자")
	LocalDateTime lastPwChangedDate,
	@Schema(description = "사용자 이름", example = "안유진")
	String name,
	@Schema(description = "유저 가입 유형", example = "KAKAO", required = true)
	UserType userType,
	@Schema(description = "광고 동의 여부", required = true)
	boolean adAgreed,
	@Schema(description = "개인정보 사용 동의 여부", required = true)
	boolean analysisAgreed,
	@Schema(description = "아바타 식별자 (아바타 = 유저 개념)")
	Long id,
	@Schema(description = "현재 쉐피 코인 개수")
	int cheffiCoinCount,
	@Schema(description = "현재 포인트 양")
	int pointCnt,
	@Schema(description = "유저 닉네임")
	String nickname,
	@Schema(description = "프로필 URL")
	String photoUrl,
	@Schema(description = "프로필 등록 완료 여부")
	boolean profileCompleted,
	@Schema(description = "유저의 권한", required = true)
	List<GrantedAuthority> authorities,
	@Schema(description = "신규 유저 여부", required = true)
	boolean isNewUser
) {

	public static OidcLoginResponse of(AuthenticationToken token, Avatar avatar, ProfilePhoto photo,
		boolean isNewUser) {
		UserPrincipal principal = (UserPrincipal)token.getPrincipal();
		return OidcLoginResponse.builder()
			.email(principal.getEmail())
			.locked(principal.isLocked())
			.expired(principal.isExpired())
			.activated(principal.isActivated())
			.lastPwChangedDate(principal.getLastPwChangedDate())
			.name(principal.getName())
			.userType(principal.getUserType())
			.adAgreed(principal.isAdAgreed())
			.analysisAgreed(principal.isAnalysisAgreed())
			.id(principal.getAvatarId())
			.cheffiCoinCount(avatar.getCheffiCoinCnt())
			.pointCnt(avatar.getPointCnt())
			.nickname(principal.stringNickname())
			.photoUrl(photo != null ? photo.getUrl() : null)
			.profileCompleted(isProfileCompleted(principal.getAuthorities()))
			.authorities((new ArrayList<>(principal.getAuthorities())))
			.isNewUser(isNewUser)
			.build();
	}

	private static boolean isProfileCompleted(Collection<? extends GrantedAuthority> authorities) {
		return !authorities.contains(new SimpleGrantedAuthority(RoleType.NO_PROFILE.getAuthority()));
	}
}

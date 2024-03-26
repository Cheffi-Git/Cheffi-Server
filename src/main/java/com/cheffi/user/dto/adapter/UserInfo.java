package com.cheffi.user.dto.adapter;

import java.time.LocalDateTime;
import java.util.List;

import com.cheffi.user.constant.UserType;
import com.cheffi.user.domain.Role;
import com.cheffi.user.domain.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserInfo(
	@Schema(description = "이메일", example = "kim12345@naver.com", required = true)
	String email,
	@Schema(description = "마지막 비밀번호 변경 시각", example = "2023-03-01 16:31:04.019")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "Asia/Seoul")
	LocalDateTime lastPwChangedDate,
	@Schema(description = "가입 시각", example = "2023-03-01 16:31:04.019", required = true)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "Asia/Seoul")
	LocalDateTime createdDate,
	@Schema(description = "이름", example = "안유진")
	String name,
	@Schema(description = "유저가 가입한 플랫폼", example = "KAKAO", required = true)
	UserType userType,
	@Schema(description = "광고 수신 동의 여부", example = "true", required = true)
	boolean adAgreed,
	@Schema(description = "개인정보 분석 이용 동의 여부", example = "true", required = true)
	boolean analysisAgreed,
	@Schema(description = "역할", example = "USER", required = true)
	List<String> authorities,
	@JsonIgnore
	String fcmToken
) {

	public static UserInfo of(User user, List<Role> roles) {
		return UserInfo.builder()
			.email(user.getEmail())
			.lastPwChangedDate(user.getLastPwChangedDate())
			.createdDate(user.getCreatedDate())
			.name(user.getName())
			.userType(user.getUserType())
			.adAgreed(user.isAdAgreed())
			.analysisAgreed(user.isAnalysisAgreed())
			.authorities(roles.stream().map(Role::getAuthority).toList())
			.fcmToken(user.getFcmToken())
			.build();
	}

	public static UserInfo of(User user) {
		return UserInfo.builder()
			.email(user.getEmail())
			.lastPwChangedDate(user.getLastPwChangedDate())
			.createdDate(user.getCreatedDate())
			.name(user.getName())
			.userType(user.getUserType())
			.adAgreed(user.isAdAgreed())
			.analysisAgreed(user.isAnalysisAgreed())
			.fcmToken(user.getFcmToken())
			.build();
	}

}

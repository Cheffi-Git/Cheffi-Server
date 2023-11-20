package com.cheffi.common.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {

	TEST(HttpStatus.INTERNAL_SERVER_ERROR, "001", "business exception test"),

	//인증
	//401 에러
	TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A-001", "토큰이 만료되었습니다."),
	NOT_VALID_TOKEN(HttpStatus.UNAUTHORIZED, "A-002", "해당 토큰은 유효한 토큰이 아닙니다."),
	MALFORMED_JWT(HttpStatus.UNAUTHORIZED, "A-003", "올바른 JWT 형식이 아닙니다."),
	NOT_SUPPORTED_JWT(HttpStatus.UNAUTHORIZED, "A-004", "지원되지 않는 JWS 입니다."),
	INVALID_PAYLOAD(HttpStatus.UNAUTHORIZED, "A-005", "페이로드 검증에 실패했습니다."),
	JWT_VERIFY_FAILED(HttpStatus.UNAUTHORIZED, "A-006", "JWT 시그니처가 올바르지 않습니다."),
	NOT_ACCESS_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "A-007", "해당 토큰은 ACCESS TOKEN이 아닙니다."),
	ANONYMOUS_USER_CANNOT_ACCESS_LOCKED_REVIEW(HttpStatus.INTERNAL_SERVER_ERROR, "A-011", "비회원은 잠긴 리뷰를 조회할 수 없습니다."),

	//403 에러
	FORBIDDEN_ADMIN(HttpStatus.FORBIDDEN, "A-008", "관리자 Role이 아닙니다."),

	//400 에러
	NOT_SUPPORTED_OAUTH_PROVIDER(HttpStatus.BAD_REQUEST, "A-009", "지원하지 않는 OAuth 프로바이더 입니다."),

	// 500 에러
	EMAIL_LOGIN_NOT_SUPPORTED(HttpStatus.INTERNAL_SERVER_ERROR, "A-010", "UserType 이 LOCAL입니다. (일반 로그인은 지원하지 않습니다.)"),

	// 회원
	INVALID_MEMBER_TYPE(HttpStatus.BAD_REQUEST, "M-001", "잘못된 회원 타입입니다. (memberType : KAKAO)"),
	ALREADY_REGISTERED_MEMBER(HttpStatus.BAD_REQUEST, "M-002", "이미 가입된 회원 입니다."),
	USER_NOT_EXISTS(HttpStatus.BAD_REQUEST, "M-003", "해당 회원은 존재하지 않습니다."),
	AVATAR_NOT_EXISTS(HttpStatus.BAD_REQUEST, "M-004", "해당 아바타는 존재하지 않습니다."),
	NICKNAME_CONTAINS_BANNED_WORDS(HttpStatus.BAD_REQUEST, "M-006", "해당 닉네임은 금지어를 포함하고 있습니다."),
	NICKNAME_ALREADY_IN_USE(HttpStatus.BAD_REQUEST, "M-007", "해당 닉네임은 이미 사용중입니다."),
	INVALID_NICKNAME_LENGTH(HttpStatus.BAD_REQUEST, "M-008", "닉네임의 길이가 올바르지 않습니다. [최소 2자, 최대 8자]"),
	PROFILE_ALREADY_REGISTERED(HttpStatus.BAD_REQUEST, "M-009", "해당 계정은 이미 프로필 등록이 완료되었습니다."),
	PROFILE_NOT_COMPLETED(HttpStatus.BAD_REQUEST, "M-010", "해당 계정의 프로필 등록이 완료된 상태가 아닙니다."),

	//팔로우
	ALREADY_FOLLOWED(HttpStatus.BAD_REQUEST, "FW-001", "해당 아바타는 이미 팔로우 중입니다."),
	NOT_FOLLOWED(HttpStatus.BAD_REQUEST, "FW-002", "해당 아바타는 팔로우 상태가 아닙니다."),

	// 식당, 리뷰
	RESTAURANT_NOT_EXIST(HttpStatus.BAD_REQUEST, "R-001", "식별자에 해당하는 식당이 없습니다."),
	TOO_MANY_MENUS(HttpStatus.BAD_REQUEST, "R-002", "한 리뷰의 메뉴는 5개를 초과할 수 없습니다."),
	RESTAURANT_DATA_NOT_EXIST(HttpStatus.BAD_REQUEST, "R-003", "공공 데이터에 "
		+ "식별자에 해당하는 식당 데이터가 없습니다."),
	RESTAURANT_ALREADY_REGISTERED(HttpStatus.BAD_REQUEST, "R-003", "해당 데이터로 생성된 식당이 이미 존재합니다."),
	REVIEW_NOT_EXIST(HttpStatus.BAD_REQUEST, "R-004", "해당 식별자는 올바른 리뷰의 식별자가 아닙니다."),
	REVIEW_NOT_UNLOCKED(HttpStatus.BAD_REQUEST, "R-005", "유저가 해당 리뷰를 구매하지 않았습니다."),
	REVIEW_ALREADY_UNLOCKED(HttpStatus.BAD_REQUEST, "R-006", "유저가 해당 리뷰를 이미 구매했습니다."),
	ALREADY_BOOKMARKED(HttpStatus.BAD_REQUEST, "R-007", "유저가 해당 리뷰를 이미 북마크했습니다."),
	NOT_BOOKMARKED(HttpStatus.BAD_REQUEST, "R-008", "유저가 해당 리뷰를 북마크 하지 않았습니다."),
	ADDRESS_NOT_EXIST(HttpStatus.BAD_REQUEST, "R-009", "존재하지 않는 주소입니다."),
	REVIEW_NOT_EXIST_IN_AREA(HttpStatus.BAD_REQUEST, "R-010", "해당 지역에 등록된 리뷰가 없습니다."),
	NOT_REVIEW_WRITER(HttpStatus.BAD_REQUEST, "R-011", "해당 지역에 등록된 리뷰가 없습니다."),

	// 파일
	NOT_IMAGE_FILE(HttpStatus.BAD_REQUEST, "F-001", "전송된 파일의 형식이 이미지가 아닙니다."),

	// 태그
	BAD_AVATAR_TAG_REQUEST(HttpStatus.BAD_REQUEST, "T-001", "아바타 관련 태그 요청이 잘못 됐습니다."),

	// 쉐피코인
	NOT_ENOUGH_CHEFFI_COIN(HttpStatus.BAD_REQUEST, "C-001", "쉐피코인이 부족합니다."),

	//기타
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "I-001", "내부 에러가 발생했습니다."),
	AVATAR_ALREADY_EXIST(HttpStatus.INTERNAL_SERVER_ERROR, "I-002", "아바타가 이미 존재합니다.");

	ErrorCode(HttpStatus httpStatus, String code, String message) {
		this.httpStatus = httpStatus;
		this.code = code;
		this.message = message;
	}

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}

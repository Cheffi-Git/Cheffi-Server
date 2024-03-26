package com.cheffi.cs.constant;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(enumAsRef = true,
	description = """
		* `SIGN_UP` - 회원가입
				
		* `REVIEW` - 리부관리
				
		* `USE` - 이용문의
				
		* `ETC` - 기타
				
		* `BEST` - 베스트
				
		* `ALL` - 전체
		""")
public enum QnaCategory {
	SIGN_UP("회원가입"), REVIEW("리뷰관리"), USE("이용문의"), ETC("기타"), BEST("BEST 10"), ALL("전체보기");

	private final String title;

	private QnaCategory(String title) {
		this.title = title;
	}
}

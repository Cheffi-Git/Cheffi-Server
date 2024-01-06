package com.cheffi.util.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import lombok.Getter;

/**
 * 시간 단위로 기간을 나타내는 객체
 */
@Getter
public class ExactPeriod {

	private final LocalDateTime start;
	private final LocalDateTime end;
	private final Duration duration;

	/**
	 * @param unit	   기준 단위 시간
	 *                 Cache의 TTL로 동작한다.
	 * @param end      기준이 되는 끝 시간
	 *                 unit 이하의 정보는 버려진다.
	 * @param duration Duration 은 unit 단위로 입력해야 정상적으로 동작한다.
	 *                 start와 end의 차이이다.
	 */

	public ExactPeriod(LocalDateTime end, Duration duration, ChronoUnit unit) {
		// 받은 시간을 정각으로 변환
		end = end.truncatedTo(unit);
		this.start = end.minus(duration);
		this.end = end;
		this.duration = duration;
	}
}

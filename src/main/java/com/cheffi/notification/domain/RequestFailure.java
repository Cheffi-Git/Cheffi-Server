package com.cheffi.notification.domain;

import com.cheffi.common.domain.BaseTimeEntity;
import com.cheffi.notification.dto.MessageResponse;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RequestFailure extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String data;

	public RequestFailure(String data) {
		this.data = data;
	}

	public static RequestFailure from(MessageResponse mr) {
		return new RequestFailure(mr.getData().toString());
	}

}

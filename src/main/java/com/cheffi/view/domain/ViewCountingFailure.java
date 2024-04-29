package com.cheffi.view.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ViewCountingFailure {

	@Id
	private Long id;

	private int count;

	public ViewCountingFailure(Long id, int count) {
		this.id = id;
		this.count = count;
	}

	public void increaseCount() {
		this.count++;
	}

}

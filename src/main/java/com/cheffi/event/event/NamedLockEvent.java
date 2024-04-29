package com.cheffi.event.event;

import lombok.Getter;

@Getter
public class NamedLockEvent {

	private final String lockName;

	public NamedLockEvent(String lockName) {
		this.lockName = lockName;
	}

}

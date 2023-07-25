package com.cheffi.common.constant;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.cheffi.common.mock.MockAddress;

class AddressTest {

	private Address mockAddressSubclass = new MockAddress();
	private Address simpleAddress = new Address();

	@Test
	void givenChildOfAddressItself() {
		assertThat(mockAddressSubclass.isSimpleAddress()).isFalse();
	}

	@Test
	void givenAddressItself() {
		assertThat(simpleAddress.isSimpleAddress()).isTrue();
	}
}

package com.cheffi.avatar.domain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class FollowTest {

	@Mock
	private Avatar subject;
	@Mock
	private Avatar target;

	@Nested
	@DisplayName("createFollowRelationship")
	class CreateFollowRelationship {

		@Test
		@DisplayName("success- 서로 다른 avatar면 성공")
		void givenValidAvatar() {

			Follow mockFollow = mock(Follow.class);

			try (MockedStatic<Follow> follow = Mockito.mockStatic(Follow.class)) {
				follow.when(() -> Follow.createFollowRelationship(subject, target))
					.thenReturn(mockFollow);

				Follow result = Follow.createFollowRelationship(subject, target);
				assertEquals(result, mockFollow);
			}
		}

		@Test
		@DisplayName("fail- 동일한 avatar가 들어오면 IllegalArgumentException 발생")
		void givenSameSubjectAndTarget_thenThrowsIllegalArgumentException() {

			assertThrows(IllegalArgumentException.class, () -> {
				Follow.createFollowRelationship(subject, subject);
			});
		}

		@Test
		@DisplayName("fail- null인 Avatar가 존재하면 NullPointerException 발생")
		void givenValidAvatar2() {

			assertThrows(NullPointerException.class, () -> {
				Follow.createFollowRelationship(null, target);
			});
		}

	}

}
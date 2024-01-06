package com.cheffi.review.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cheffi.review.domain.Menu;
import com.cheffi.review.domain.Review;
import com.cheffi.review.dto.MenuDto;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

	private final MenuService menuService = new MenuService();

	@Mock
	private Review review;

	private MenuDto ramenDto;
	private MenuDto riceDto;
	private MenuDto curryDto;
	private List<MenuDto> menuDtos;

	@BeforeEach
	void setUp() {
		ramenDto = new MenuDto("라면", 5000, "신라면");
		riceDto = new MenuDto("볶음밥", 8000, "중화 볶음밥");
		curryDto = new MenuDto("커리", 10000, "인도식 커리");
		menuDtos = List.of(ramenDto, riceDto, curryDto);
	}

	@Nested
	@DisplayName("addMenus 메서드")
	class AddMenus {

		@DisplayName("addMenus 메서드를 호출하면 추가된 Menu 를 반환한다.")
		@Test
		void givenMenuDto() {
			//when
			List<Menu> result = menuService.addMenus(review, menuDtos);

			//then
			assertThat(result).hasSize(3);
			for (MenuDto dto : menuDtos) {
				Optional<Menu> optionalMenu = result.stream().filter(m -> m.getName().equals(dto.name())).findFirst();
				assertThat(optionalMenu).isPresent();
				Menu menu = optionalMenu.get();
				assertThat(menu.getPrice()).isEqualTo(dto.price());
				assertThat(menu.getDescription()).isEqualTo(dto.description());
			}
		}

	}

}

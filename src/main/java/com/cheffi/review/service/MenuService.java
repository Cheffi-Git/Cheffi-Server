package com.cheffi.review.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.review.domain.Menu;
import com.cheffi.review.domain.Review;
import com.cheffi.review.dto.MenuDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MenuService {

	@Transactional
	public List<Menu> addMenus(Review review, List<MenuDto> menuDtos) {
		List<Menu> menus = new ArrayList<>();
		for (var menuDto : menuDtos) {
			menus.add(new Menu(menuDto.name(), menuDto.price(), menuDto.description(), review));
		}

		review.addMenus(menus);
		return menus;
	}

	@Transactional
	public List<Menu> changeMenus(Review review, List<MenuDto> menuDtos) {
		review.clearMenus();
		return addMenus(review, menuDtos);
	}

}

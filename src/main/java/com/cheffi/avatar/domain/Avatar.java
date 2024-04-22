package com.cheffi.avatar.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.BusinessException;
import com.cheffi.common.constant.Address;
import com.cheffi.common.domain.BaseTimeEntity;
import com.cheffi.tag.constant.TagType;
import com.cheffi.tag.domain.Tag;
import com.cheffi.user.domain.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Avatar extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Valid
	@NotNull
	@Embedded
	private Nickname nickname;
	private String introduction;
	@Embedded
	private Address address;
	private int cheffiCoinCnt;
	private int pointCnt;
	private int followerCnt;
	private int followingCnt;
	private int postCnt;

	@NotNull
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "photo_id")
	private ProfilePhoto photo;

	@OneToMany(mappedBy = "avatar", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AvatarTag> avatarTags = new ArrayList<>();

	public Avatar(User user) {
		this.nickname = Nickname.getRandom();
		this.user = user;
		this.cheffiCoinCnt = 0;
		this.pointCnt = 0;
		this.followerCnt = 0;
		this.followingCnt = 0;
		this.postCnt = 0;
	}

	public void changeAddress(Address address) {
		if (!address.isSimpleAddress())
			throw new IllegalArgumentException("Address 클래스의 서브 클래스는 사용할 수 없습니다.");
		this.address = address;
	}

	public void changeNickname(String newNickname) {
		this.nickname = nickname.updateOf(newNickname);
	}

	public String stringNickname() {
		return nickname.getValue();
	}

	public void changePhoto(ProfilePhoto photo) {
		Assert.notNull(photo, "변경할 사진은 null 일 수 없습니다.");
		this.photo = photo;
	}

	public void changeIntroduction(String introduction) {
		if (!StringUtils.hasText(introduction)) {
			this.introduction = null;
			return;
		}
		if (introduction.length() < 10 || introduction.length() > 50)
			throw new BusinessException(ErrorCode.INVALID_INTRO_LENGTH);
		this.introduction = introduction;
	}

	public boolean hasPhoto() {
		return this.getPhoto() != null;
	}

	public void addTags(List<Tag> tagsToAdd) {
		List<Tag> tagList = avatarTags.stream().map(AvatarTag::getTag).toList();
		tagsToAdd.stream()
			.filter(t -> !tagList.contains(t))
			.map(t -> AvatarTag.mapTagToAvatar(this, t))
			.forEach(avatarTags::add);
	}

	public void removeTags(List<Tag> tagsToRemove) {
		avatarTags.removeIf(at -> tagsToRemove.contains(at.getTag()));
	}

	public boolean hasTags() {
		return !avatarTags.isEmpty();
	}

	public List<Tag> getTags(TagType type) {
		return this.avatarTags.stream()
			.map(AvatarTag::getTag)
			.filter(tag -> tag.hasType(type))
			.toList();
	}

	void applyCheffiCoinBy(int value) {
		if (cheffiCoinCnt + value < 0)
			throw new BusinessException(ErrorCode.NOT_ENOUGH_CHEFFI_COIN);
		this.cheffiCoinCnt += value;
	}

	public boolean hasSameIdWith(Long idToCompare) {
		return getId().equals(idToCompare);
	}

	public void addFollower() {
		this.followerCnt++;
	}

	public void addFollowing() {
		this.followingCnt++;
	}

	public void removeFollower() {
		this.followerCnt--;
	}

	public void removeFollowing() {
		this.followingCnt--;
	}

	public void addPostCount() {
		this.postCnt++;
	}
}

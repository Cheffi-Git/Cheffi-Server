package com.cheffi.avatar.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cheffi.avatar.domain.Avatar;
import com.cheffi.avatar.domain.ProfilePhoto;
import com.cheffi.avatar.dto.adapter.SelfAvatarInfo;
import com.cheffi.avatar.dto.response.AvatarInfoResponse;
import com.cheffi.avatar.repository.AvatarJpaRepository;
import com.cheffi.avatar.repository.AvatarRepository;
import com.cheffi.common.aspect.annotation.UpdatePrincipal;
import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.BusinessException;
import com.cheffi.common.config.exception.business.EntityNotFoundException;
import com.cheffi.profile.dto.MyPageInfo;
import com.cheffi.profile.dto.ProfileInfo;
import com.cheffi.user.domain.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AvatarService {

	private final AvatarRepository avatarRepository;
	private final AvatarJpaRepository avatarJpaRepository;
	private final ProfilePhotoService profilePhotoService;

	@UpdatePrincipal
	@Transactional
	public SelfAvatarInfo updateNickname(Long avatarId, String nickname) {
		if (isNicknameInUse(nickname))
			throw new BusinessException(ErrorCode.NICKNAME_ALREADY_IN_USE);
		Avatar avatar = getById(avatarId);
		avatar.changeNickname(nickname);
		return SelfAvatarInfo.of(avatar);
	}

	@Transactional
	public String changePhotoTab(Long avatarId, String introduction, MultipartFile file, boolean defaultPhoto) {
		Avatar avatar = getById(avatarId);

		avatar.changeIntroduction(introduction);
		return changePhoto(file, defaultPhoto, avatar);
	}

	public boolean isNicknameInUse(String nickname) {
		return avatarRepository.existsByNickname(nickname);
	}

	public Avatar createAvatar(User user) {
		Avatar avatar = new Avatar(user);
		for (int i = 0; i < 10; i++) {
			if (!isNicknameInUse(avatar.stringNickname()))
				break;
			avatar = new Avatar(user);
		}
		if (isNicknameInUse(avatar.stringNickname()))
			throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
		profilePhotoService.addDefaultPhoto(avatar);
		return avatarRepository.save(avatar);
	}

	@UpdatePrincipal
	public SelfAvatarInfo getSelfAvatarInfo(Long avatarId) {
		Avatar avatar = getByIdWithPhoto(avatarId);
		return SelfAvatarInfo.of(avatar, avatar.getPhoto());
	}

	@Transactional
	public String changePhoto(MultipartFile file, boolean defaultPhoto, Avatar avatar) {
		String s3key = null;
		if (avatar.hasPhoto())
			s3key = profilePhotoService.deletePhotoFromDB(avatar);

		ProfilePhoto addedPhoto;
		if (defaultPhoto)
			addedPhoto = profilePhotoService.addDefaultPhoto(avatar);
		else
			addedPhoto = profilePhotoService.addPhoto(avatar, file);

		try {
			profilePhotoService.deletePhotoFromS3(s3key);
		} catch (Exception e) {
			profilePhotoService.deletePhotoFromS3(addedPhoto.getS3Key());
			throw new BusinessException(e.getMessage());
		}

		return addedPhoto.getUrl();
	}

	public boolean checkIfCompleteProfile(Long avatarId) {
		Avatar avatar = getByIdWithTagsAndPhoto(avatarId);
		return avatar.hasTags() &&
			   avatar.hasPhoto();
	}

	public AvatarInfoResponse getAvatarInfo(Long avatarId) {
		return AvatarInfoResponse.of(getByIdWithTagsAndPhoto(avatarId));
	}

	public MyPageInfo getMyPageInfo(Long avatarId) {
		return MyPageInfo.of(getByIdWithTagsAndPhoto(avatarId));
	}

	public ProfileInfo getProfile(Long ownerId, Long viewerId) {
		return avatarJpaRepository.findProfile(ownerId, viewerId);
	}

	public Avatar getById(Long avatarId) {
		return avatarRepository.findById(avatarId)
			.orElseThrow(() -> new EntityNotFoundException(ErrorCode.AVATAR_NOT_EXISTS));
	}

	public Avatar getByUserWithPhoto(User user) {
		return avatarRepository.findByUserWithPhoto(user)
			.orElseThrow(() -> new EntityNotFoundException(ErrorCode.AVATAR_NOT_EXISTS));
	}

	public Avatar getByIdWithPhoto(Long avatarId) {
		return avatarRepository.findByIdWithPhoto(avatarId)
			.orElseThrow(() -> new EntityNotFoundException(ErrorCode.AVATAR_NOT_EXISTS));
	}

	public Avatar getByIdWithTagsAndPhoto(Long avatarId) {
		return avatarRepository.findByIdWithTagsAndPhoto(avatarId)
			.orElseThrow(() -> new EntityNotFoundException(ErrorCode.AVATAR_NOT_EXISTS));
	}

	public List<Avatar> getAllActive() {
		return avatarRepository.findAllActive();
	}

}

package com.cheffi.avatar.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cheffi.avatar.domain.Avatar;
import com.cheffi.avatar.domain.ProfilePhoto;
import com.cheffi.avatar.dto.adapter.SelfAvatarInfo;
import com.cheffi.avatar.dto.response.AvatarInfoResponse;
import com.cheffi.avatar.repository.AvatarRepository;
import com.cheffi.common.aspect.annotation.UpdatePrincipal;
import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.BusinessException;
import com.cheffi.common.config.exception.business.EntityNotFoundException;
import com.cheffi.user.domain.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AvatarService {

	private final AvatarRepository avatarRepository;
	private final ProfilePhotoService profilePhotoService;

	@UpdatePrincipal
	@Transactional
	public SelfAvatarInfo updateNickname(Long avatarId, String nickname) {
		if (nickname.contains("쉐피"))
			throw new BusinessException(ErrorCode.NICKNAME_CONTAINS_BANNED_WORDS);
		if (isNicknameInUse(nickname))
			throw new BusinessException(ErrorCode.NICKNAME_ALREADY_IN_USE);
		Avatar avatar = getById(avatarId);
		avatar.changeNickname(nickname);
		return SelfAvatarInfo.of(avatar);
	}

	public boolean isNicknameInUse(String nickname) {
		return avatarRepository.existsByNickname(nickname);
	}

	@UpdatePrincipal
	public SelfAvatarInfo getSelfAvatarInfo(Long avatarId) {
		Avatar avatar = getByIdWithPhoto(avatarId);
		return SelfAvatarInfo.of(avatar, avatar.getPhoto());
	}

	@Transactional
	public String changePhoto(Long avatarId, MultipartFile file, boolean defaultPhoto) {
		Avatar avatar = getByIdWithPhoto(avatarId);
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
			avatar.hasPhoto() &&
			!avatar.getNickname().contains("쉐피");
	}

	public AvatarInfoResponse getAvatarInfo(Long avatarId) {
		return AvatarInfoResponse.mock();
	}

	public Avatar getById(Long avatarId) {
		return avatarRepository.findById(avatarId)
			.orElseThrow(() -> new EntityNotFoundException(ErrorCode.AVATAR_NOT_EXISTS));
	}

	public boolean existById(Long avatarId) {
		return avatarRepository.existsById(avatarId);
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

}

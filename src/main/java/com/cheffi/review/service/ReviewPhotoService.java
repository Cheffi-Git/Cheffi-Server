package com.cheffi.review.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cheffi.common.config.exception.business.FileUploadException;
import com.cheffi.common.constant.S3RootPath;
import com.cheffi.common.dto.ImageFileInfo;
import com.cheffi.common.service.FileUploadService;
import com.cheffi.review.domain.Review;
import com.cheffi.review.domain.ReviewPhoto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ReviewPhotoService {
	private final FileUploadService fileUploadService;

	@Transactional
	public List<ReviewPhoto> addPhotos(Review review, List<MultipartFile> images) {
		List<ImageFileInfo> imageFileInfos = new ArrayList<>();
		List<ReviewPhoto> reviewPhotos = new ArrayList<>();

		try {
			for (var image : images) {
				imageFileInfos.add(fileUploadService.uploadImageToS3(image, S3RootPath.REVIEW_PHOTO));
			}
			for (int i = 0; i < imageFileInfos.size(); i++) {
				reviewPhotos.add(imageFileInfos.get(i).toReviewPhoto(review, i));
			}
			review.addPhotos(reviewPhotos);
		} catch (Exception e) {
			for (var imageInfo : imageFileInfos) {
				fileUploadService.removeFromS3(imageInfo.S3key());
			}
			throw new FileUploadException(e.getMessage());
		}

		return reviewPhotos;
	}

	@Transactional
	public void changePhotos(Review review, List<MultipartFile> images, S3RootPath originalRootPath) {
		removePhotos(review, originalRootPath);
		addPhotos(review, images);
	}

	@Transactional
	public void removePhotos(Review review, S3RootPath originalRootPath) {
		List<String> s3Keys = review.getPhotos()
			.stream()
			.map(ReviewPhoto::getS3Key)
			.toList();

		List<String> backupS3Keys = s3Keys
			.stream()
			.map(s3Key -> fileUploadService.copyInS3(s3Key, originalRootPath, S3RootPath.BACKUP))
			.toList();

		List<String> removedS3Keys = new ArrayList<>(s3Keys.size());

		try {
			for (String s3Key : s3Keys) {
				fileUploadService.removeFromS3(s3Key);
				removedS3Keys.add(s3Key);
			}
		} catch (Exception e) {
			for (String removedS3Key : removedS3Keys) {
				String toRestoreS3Key = removedS3Key.replaceFirst(originalRootPath.getPath(), S3RootPath.BACKUP.getPath());

				if (backupS3Keys.contains(toRestoreS3Key))
					fileUploadService.copyInS3(toRestoreS3Key, S3RootPath.BACKUP, originalRootPath);
			}

			backupS3Keys.forEach(fileUploadService::removeFromS3);

			throw new FileUploadException(e.getMessage());
		}

		review.clearPhotos();
	}
}

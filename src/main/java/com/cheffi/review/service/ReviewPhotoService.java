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
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
	public void deletePhotos(List<ReviewPhoto> photos) {
		String s3Key = null;
		try {
			for (var photo : photos) {
				s3Key = photo.getS3Key();
				fileUploadService.removeFromS3(s3Key);
			}
		} catch (Exception e) {
			log.error("Exception occurred for photo with s3Key {}", s3Key);
		}
	}

	@Transactional
	public List<ReviewPhoto> changePhotos(Review review, List<MultipartFile> images) {
		List<ReviewPhoto> photosToDelete = review.getPhotos();
		review.getPhotos().clear();
		List<ReviewPhoto> result = addPhotos(review, images);
		deletePhotos(photosToDelete);

		return result;
	}
}
package com.cheffi.review.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.cheffi.common.constant.S3RootPath;
import com.cheffi.common.dto.ImageFileInfo;
import com.cheffi.common.service.FileUploadService;
import com.cheffi.review.domain.Review;
import com.cheffi.review.domain.ReviewPhoto;

@ExtendWith(MockitoExtension.class)
class ReviewPhotoServiceTest {

	@Mock
	private Review review;
	@Mock
	private FileUploadService fileUploadService;
	@InjectMocks
	private ReviewPhotoService reviewPhotoService;

	@Mock
	private MultipartFile image1;
	@Mock
	private MultipartFile image2;
	@Mock
	private MultipartFile image3;
	private List<MultipartFile> images;

	@BeforeEach
	void setUp() {
		images = List.of(image1, image2, image3);
	}

	@Nested
	@DisplayName("addPhotos 메서드")
	class AddPhotos {

		@Mock
		private ImageFileInfo imageFileInfo1;
		@Mock
		private ImageFileInfo imageFileInfo2;
		@Mock
		private ImageFileInfo imageFileInfo3;

		private List<ImageFileInfo> imageFileInfos;

		@Mock
		private ReviewPhoto reviewPhoto1;
		@Mock
		private ReviewPhoto reviewPhoto2;
		@Mock
		private ReviewPhoto reviewPhoto3;
		private List<ReviewPhoto> reviewPhotos;

		@BeforeEach
		void setUp() {
			reviewPhotos = List.of(reviewPhoto1, reviewPhoto2, reviewPhoto3);
			imageFileInfos = List.of(imageFileInfo1, imageFileInfo2, imageFileInfo3);

			for (int i = 0; i < 3; i++) {
				when(fileUploadService.uploadImageToS3(images.get(i), S3RootPath.REVIEW_PHOTO))
					.thenReturn(imageFileInfos.get(i));
			}

			for (int i = 0; i < imageFileInfos.size(); i++) {
				when(imageFileInfos.get(i).toReviewPhoto(review, i)).thenReturn(reviewPhotos.get(i));
			}

		}

		@Test
		@DisplayName("사진을 추가하면 반영된다.")
		void givenValidPhotos() {
			//when
			List<ReviewPhoto> result = reviewPhotoService.addPhotos(review, images);

			//then
			verify(review, times(1)).addPhotos(any());
			assertThat(result).hasSize(3).containsAll(reviewPhotos);
		}

	}

}

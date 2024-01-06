package com.cheffi.common.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.cheffi.common.config.exception.business.BusinessException;
import com.cheffi.common.constant.S3RootPath;
import com.cheffi.common.dto.ImageFileInfo;
import com.cheffi.common.dto.ImageSize;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileUploadService {

	private final String bucketName;
	private final AmazonS3 amazonS3;
	private final ImagePathStrategy imagePathStrategy;

	public FileUploadService(@Value("${cloud.aws.s3.bucket}") String bucketName,
		AmazonS3 amazonS3, ImagePathStrategy imagePathStrategy) {
		this.bucketName = bucketName;
		this.amazonS3 = amazonS3;
		this.imagePathStrategy = imagePathStrategy;
	}

	public ImageFileInfo uploadImageToS3(MultipartFile file, S3RootPath path) {
		String key = getKey(file, path);
		ImageSize imageSize = getImageSize(file);
		String fileUrl = uploadFileToS3(file, key);
		return ImageFileInfo.of(fileUrl, key, imageSize);
	}

	public String copyInS3(String s3Key, S3RootPath rootPath, S3RootPath copyRootPath) {
		String backupS3Key = s3Key.replaceFirst(rootPath.getPath(), copyRootPath.getPath());
		CopyObjectRequest copyObjectRequest = new CopyObjectRequest(bucketName,s3Key,bucketName, backupS3Key);
		amazonS3.copyObject(copyObjectRequest);

		return backupS3Key;
	}

	public String getKey(MultipartFile file, S3RootPath path) {
		String rootPath = path.getPath();
		String type = imagePathStrategy.getType(file);
		String fileName = imagePathStrategy.getFileName();

		return rootPath + "/" + fileName + "." + type;
	}

	public ImageSize getImageSize(MultipartFile file) {
		try (InputStream inputStream = file.getInputStream()) {
			BufferedImage read = ImageIO.read(inputStream);
			int width = read.getWidth();
			int height = read.getHeight();
			Long size = file.getSize();
			return new ImageSize(width, height, size);
		} catch (IOException e) {
			log.warn(e.toString());
			throw new BusinessException(e.getMessage());
		}
	}

	private String uploadFileToS3(MultipartFile file, String key) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(file.getSize());
		metadata.setContentType(file.getContentType());

		try (InputStream inputStream = file.getInputStream()) {
			PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream, metadata);
			amazonS3.putObject(putObjectRequest);
			return amazonS3.getUrl(bucketName, key).toString();
		} catch (IOException e) {
			log.warn(e.toString());
			throw new BusinessException(e.getMessage());
		}
	}

	public void removeFromS3(String s3Key) {
		amazonS3.deleteObject(bucketName, s3Key);
	}

}

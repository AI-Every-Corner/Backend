package com.aieverywhere.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import com.aieverywhere.backend.models.Images;
import com.aieverywhere.backend.repostories.ImageRepo;
import com.aieverywhere.backend.repostories.ImageSpecifications;

@Service
public class ImagesServices {

	@Value("${app.upload.dir}")
	private String uploadDir;
	private final ImageRepo imageRepo;

	@Autowired
	public ImagesServices(ImageRepo imageRepo) {
		this.imageRepo = imageRepo;
	}

	public String createImage(Images image) {
		imageRepo.save(image);
		return "create success";
	}

	public String deleteImage(Long imageId) throws Exception {
		imageRepo.deleteById(imageId);
		return "delete success";
	}

	public Images findImageById(Long imageId) throws Exception {
		try {
			return imageRepo.findOne(ImageSpecifications.hasImageId(imageId)).get();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String uploadImage(MultipartFile file) throws Exception {
		try {
			String originalFileName = file.getOriginalFilename();
			System.out.println("Original file name: " + originalFileName);

			// 獲得文件格式
			String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
			String fileName = UUID.randomUUID().toString() + suffix;

			// 確保上傳目錄存在
			File uploadDirFile = new File(uploadDir);
			if (!uploadDirFile.exists()) {
				uploadDirFile.mkdirs();
			}

			// 保存圖片到指定路徑
			File targetFile = new File(uploadDirFile, fileName);
			file.transferTo(targetFile);

			// 回傳圖片的 URL
			String imageUrl = "http://localhost:8080/images/" + fileName;
			System.out.println("File saved to: " + targetFile.getAbsolutePath());
			return imageUrl;
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("Failed to upload image: " + e.getMessage());
		}
	}

	public Long getUseCountSumNotUserUpload() {
		return imageRepo.sumUseCountForNonUploadedImages();
	}

	public Long getPicUseByAi() {
		return imageRepo.countByIsUploadByUserFalse();
	}

	public Long getPicCount() {
		return imageRepo.count();
	}

}
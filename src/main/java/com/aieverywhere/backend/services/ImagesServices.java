package com.aieverywhere.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;


import com.aieverywhere.backend.models.Images;
import com.aieverywhere.backend.repostories.ImageRepo;
import com.aieverywhere.backend.repostories.ImageSpecifications;

@Service
public class ImagesServices {
	private final ImageRepo imageRepo;

	@Autowired
	public ImagesServices(ImageRepo imageRepo) {
		this.imageRepo = imageRepo;
	}

	public String createImage(Images image) {
		imageRepo.save(image);
		return "create success";
	}

	public String deleteImage(Long imageId) throws Exception{
		imageRepo.deleteById(imageId);
		return "delete success";
	}
	
	public Images findImageById(Long imageId) throws Exception{
		try {
			return imageRepo.findOne(ImageSpecifications.hasImageId(imageId)).get();
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String uploadImage(MultipartFile file) throws Exception {
		try {
			String originalFileName = file.getOriginalFilename();
			System.out.println(originalFileName);
			// 獲得文件格式
			String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
			String fileName = UUID.randomUUID().toString();
			// 保存圖片到指定路徑
			file.transferTo(new File("C:\\Users\\user\\Desktop\\AI-every-coner\\picture"
					+ fileName + suffix));
			// 回傳圖片的 URL
			String imageUrl = "/images/products/" + fileName + suffix;
			return imageUrl;
		} catch (IOException e) {
			e.printStackTrace();
			return "failed to upload "+e.getMessage();
		}

	}

}
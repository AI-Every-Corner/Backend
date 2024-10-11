package com.aieverywhere.backend.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aieverywhere.backend.repostories.ImageRepo;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


@Service
public class ImageService {

    @Value("${app.upload.dir}")
    private String uploadDir;

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
            String imageUrl = "https://localhost:8080/images/" + fileName;
            System.out.println("File saved to: " + targetFile.getAbsolutePath());
            return imageUrl;
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Failed to upload image: " + e.getMessage());
        }
    }
}

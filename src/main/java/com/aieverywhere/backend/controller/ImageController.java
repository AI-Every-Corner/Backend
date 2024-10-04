package com.aieverywhere.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.aieverywhere.backend.models.Images;
import com.aieverywhere.backend.services.ImagesServices;

@Controller
public class ImageController {
	private final ImagesServices imagesServices;

	public ImageController(ImagesServices imagesServices) {
		this.imagesServices = imagesServices;
	}

	@GetMapping("/findImage/{imageId}")
	public ResponseEntity<?> findImage(@PathVariable Long imageId) {
		return null;
	}

	@PostMapping("/uploadImage")
	public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
		try {
			return ResponseEntity.status(200).body("upload success " + imagesServices.uploadImage(file));
		} catch (Exception e) {
			return ResponseEntity.status(500).body("upload failed " + e.getMessage());

		}
	}

	@PutMapping("/updateImage/{imageId}")
	public ResponseEntity<?> updateImage(@PathVariable Long imageId, @RequestParam("file") MultipartFile file) {
		try {
			Images update = imagesServices.findImageById(imageId);
			String newImageUrl = imagesServices.uploadImage(file);
			update.setImagePath(newImageUrl);
			return ResponseEntity.status(200).body("update success");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("update failed " + e.getMessage());

		}
	}

	@DeleteMapping("/deleteImage/{imageId}")
	public ResponseEntity<?> deleteImage(@PathVariable Long imageId) {
		try {
			return ResponseEntity.status(200).body(imagesServices.deleteImage(imageId));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("delete failed " + e.getMessage());

		}
	}
}

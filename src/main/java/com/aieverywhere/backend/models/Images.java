package com.aieverywhere.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "images")
public class Images {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long imageId;
	private String imagePath;
	@Column(length = 5000)
	private String description;
	private Boolean isUploadByUser;
	private Boolean isAvatar;
	private Long useCount;

	public Images() {

	}

	public Images(Long imageId, String imagePath, String description, Boolean isUploadByUser, Boolean isAvatar,
			Long useCount) {
		this.imageId = imageId;
		this.imagePath = imagePath;
		this.description = description;
		this.isUploadByUser = isUploadByUser;
		this.isAvatar = isAvatar;
		this.useCount = useCount;
	}

	public Long getImageId() {
		return imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getIsUploadByUser() {
		return isUploadByUser;
	}

	public void setIsUploadByUser(Boolean isUploadByUser) {
		this.isUploadByUser = isUploadByUser;
	}

	public Boolean getIsAvatar() {
		return isAvatar;
	}

	public void setIsAvatar(Boolean isAvatar) {
		this.isAvatar = isAvatar;
	}

	public Long getUseCount() {
		return useCount;
	}

	public void setUseCount(Long useCount) {
		this.useCount = useCount;
	}

}
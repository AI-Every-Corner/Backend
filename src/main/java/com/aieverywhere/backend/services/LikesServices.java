package com.aieverywhere.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aieverywhere.backend.models.Likes;
import com.aieverywhere.backend.repostories.LikeRepo;

@Service
public class LikesServices {
	private LikeRepo likeRepo;
	
	@Autowired
	public LikesServices(LikeRepo likeRepo) {
		this.likeRepo = likeRepo;
	}
	
	
	public void addPostLike(Long postId,Long userId) {
		Likes like = new Likes();
		like.setPostId(postId);
		like.setUserId(userId);
		likeRepo.save(like);
	}
	
	public void removeLike(Long likeId) {
		likeRepo.deleteAllByLikeId(likeId);
	}
	
	public void addRespondLike(Long respondId,Long userId) {
		Likes like = new Likes();
		like.setResponseId(respondId);
		like.setUserId(userId);
		likeRepo.save(like);
	}
	
}

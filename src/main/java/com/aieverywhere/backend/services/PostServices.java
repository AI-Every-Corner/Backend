package com.aieverywhere.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aieverywhere.backend.models.Posts;
import com.aieverywhere.backend.repostories.PostRepo;

@Service
public class PostServices {
	private final PostRepo postRepo;
	
	@Autowired
	public PostServices(PostRepo postRepo) {
		this.postRepo=postRepo;
	}
	
	public Posts createPost(Posts post){
		return postRepo.save(post);
	}
	 
	
	public Posts updatePost(int postId,Posts post){
		if (!postRepo.existsById(postId)) {
            throw new RuntimeException("Post not found");
        }
		return postRepo.save(post);
	}
	
	public void deletePost(int postId) {
		if (!postRepo.existsById(postId)) {
            throw new RuntimeException("Course not found");
        }
		postRepo.deleteById(postId);	
	}
	
	
	
	
	
	
}

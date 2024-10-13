package com.aieverywhere.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aieverywhere.backend.models.Likes;
import com.aieverywhere.backend.models.Posts;
import com.aieverywhere.backend.models.Responses;
import com.aieverywhere.backend.repostories.LikeRepo;
import com.aieverywhere.backend.repostories.PostRepo;
import com.aieverywhere.backend.repostories.RespRepo;

import jakarta.persistence.EntityNotFoundException;

@Service
public class LikesServices {
	private LikeRepo likeRepo;
	private PostRepo postRepo;
	private RespRepo respRepo;
	
	@Autowired
	public LikesServices(LikeRepo likeRepo, PostRepo postRepo, RespRepo respRepo) {
		this.likeRepo = likeRepo;
		this.postRepo = postRepo;
		this.respRepo = respRepo;
	}
	
	@Transactional
	public void addPostLike(Long postId, Long userId) {
	    // Check if the user has already liked the post
	    if (likeRepo.existsByPostId(postId)) {
	        throw new IllegalStateException("User has already liked this post");
	    }

	    // Add the like
	    Likes like = new Likes();
	    like.setPostId(postId);
	    like.setUserId(userId); // who press this
	    likeRepo.save(like);

	    // Update the post's like count
	    Posts post = postRepo.findById(postId)
	        .orElseThrow(() -> new EntityNotFoundException("Post not found"));
	    
	    post.setLikes(post.getLikes() + 1);
	    postRepo.save(post);
	}

	@Transactional
	public void removePostLike(Long postId, Long likeId) {
	    // Check if the user has already liked the post
	    if (likeRepo.existsByPostId(postId)) {
			likeRepo.deleteAllByLikeId(likeId);
	
		    // Update the post's like count
		    Posts post = postRepo.findById(postId)
		        .orElseThrow(() -> new EntityNotFoundException("Post not found"));
		    
		    post.setLikes(post.getLikes() - 1);
		    postRepo.save(post);
	    }
	}
	
	public void addRespondLike(Long respId, Long postId, Long userId) {
	    // Check if the user has already liked the post
	    if (likeRepo.existsByResponseIdAndPostId(respId, postId)) {
	        throw new IllegalStateException("User has already liked this post");
	    }

	    // Add the like
	    Likes like = new Likes();
	    like.setPostId(postId);
	    like.setPostId(respId);
	    like.setUserId(userId);
	    likeRepo.save(like);

	    // Update the post's like count
	    Responses response = respRepo.findById(respId)
		        .orElseThrow(() -> new EntityNotFoundException("Response not found"));
		
	    response.setLikes(response.getLikes() + 1);
	    respRepo.save(response);
	}

	@Transactional
	public void removeRespondLike(Long likeId) {
		Likes like = new Likes();
		if (like.getPostId() != null && like.getResponseId() != null) {
			likeRepo.deleteAllByLikeId(likeId);
	
		    // Update the post's like count
		    Responses response = respRepo.findById(like.getResponseId())
		        .orElseThrow(() -> new EntityNotFoundException("Response not found"));
		    
		    response.setLikes(response.getLikes() - 1);
		    respRepo.save(response);
	    }
	}
	
}

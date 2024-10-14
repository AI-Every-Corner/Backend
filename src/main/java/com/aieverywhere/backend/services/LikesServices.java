package com.aieverywhere.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aieverywhere.backend.models.Likes;
import com.aieverywhere.backend.models.Posts;
import com.aieverywhere.backend.models.Responses;
import com.aieverywhere.backend.repostories.LikeRepo;
import com.aieverywhere.backend.repostories.LikeSpecifications;
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
		if (likeRepo.existsByPostIdAndUserId(postId, userId)) {
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
	public void removePostLike(Long postId, Long userId) {
		// Check if the user has already liked the post
		if (likeRepo.existsByPostIdAndUserId(postId, userId)) {
			Specification<Likes> likeSpec = Specification.where(LikeSpecifications.hasPostId(postId))
					.and(LikeSpecifications.hasUserId(userId));
			
			likeRepo.delete(likeSpec);

		    // Update the post's like count
		    Posts post = postRepo.findById(postId)
		        .orElseThrow(() -> new EntityNotFoundException("Post not found"));
		    
		    post.setLikes(post.getLikes() - 1);
		    postRepo.save(post);
		} else {
	        throw new EntityNotFoundException("Like not found for the given post or user");
	    }
	}
	
	public void addRespondLike(Long respId, Long userId) {
	    // Check if the user has already liked the post
		if (likeRepo.existsByResponseIdAndUserId(respId, userId)) {
		    throw new IllegalStateException("User has already liked this post");
		}

		// Add the like
		Likes like = new Likes();
		like.setResponseId(respId);
		like.setUserId(userId);
		likeRepo.save(like);

		// Update the post's like count
		Responses response = respRepo.findById(respId)
		        .orElseThrow(() -> new EntityNotFoundException("Response not found"));
		
		response.setLikes(response.getLikes() + 1);
		respRepo.save(response);
	}

	@Transactional
	public void removeRespondLike(Long responseId, Long userId) {
		if (likeRepo.existsByResponseIdAndUserId(responseId, userId)) {
	        Specification<Likes> likeSpec = Specification
	        		.where(LikeSpecifications.hasResponseId(responseId))
	                .and(LikeSpecifications.hasUserId(userId));
	        
	        Likes like = likeRepo.findOne(likeSpec)
	            .orElseThrow(() -> new EntityNotFoundException("Like not found"));

	        // Delete the like
	        likeRepo.delete(like);

	        // Update the response's like count
	        Responses response = respRepo.findById(responseId)
	            .orElseThrow(() -> new EntityNotFoundException("Response not found"));
	        
	        response.setLikes(response.getLikes() - 1);
	        respRepo.save(response);
	    } else {
	        throw new EntityNotFoundException("Like not found for the given post, response, or user");
	    }
	}
	
	public List<Likes> getLikedPostIdsByUserId(Long userId) {
		Specification<Likes> spec = LikeSpecifications.hasUserId(userId);
		return likeRepo.findAll(spec);
	}
	
}

package com.aieverywhere.backend.services;

<<<<<<< HEAD
import java.util.List;
=======
import java.time.LocalDateTime;
>>>>>>> 3e65a3c30d1437954072f1970bcb0175d129c863

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aieverywhere.backend.models.Likes;
import com.aieverywhere.backend.models.Notifications;
import com.aieverywhere.backend.models.Posts;
import com.aieverywhere.backend.models.Responses;
import com.aieverywhere.backend.models.Notifications.Type;
import com.aieverywhere.backend.repostories.LikeRepo;
import com.aieverywhere.backend.repostories.LikeSpecifications;
import com.aieverywhere.backend.repostories.NotificationRepository;
import com.aieverywhere.backend.repostories.PostRepo;
import com.aieverywhere.backend.repostories.RespRepo;

import jakarta.persistence.EntityNotFoundException;

@Service
public class LikesServices {
	private LikeRepo likeRepo;
	private PostRepo postRepo;
	private RespRepo respRepo;
	private NotificationService notificationService;

	@Autowired
	public LikesServices(LikeRepo likeRepo, PostRepo postRepo, RespRepo respRepo,
			NotificationService notificationService) {
		this.likeRepo = likeRepo;
		this.postRepo = postRepo;
		this.respRepo = respRepo;
		this.notificationService = notificationService;
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

		// this add notification
		Notifications notification = new Notifications();
		notification.setSenderId(userId);
		notification.setType(Type.Like);
		notification.setCreatedAt(LocalDateTime.now());
		notification.setUserId(postRepo.findByPostId(postId).getUserId());
		notification.setPostId(postId);

		notificationService.createContextAndSave(notification);

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
<<<<<<< HEAD
	
	public void addRespondLike(Long respId, Long userId) {
	    // Check if the user has already liked the post
		if (likeRepo.existsByResponseIdAndUserId(respId, userId)) {
		    throw new IllegalStateException("User has already liked this post");
=======

	public void addRespondLike(Long respId, Long postId, Long userId) {
		// Check if the user has already liked the post
		if (likeRepo.existsByResponseIdAndPostIdAndUserId(respId, postId, userId)) {
			throw new IllegalStateException("User has already liked this post");
>>>>>>> 3e65a3c30d1437954072f1970bcb0175d129c863
		}

		// Add the like
		Likes like = new Likes();
		like.setResponseId(respId);
		like.setUserId(userId);
		likeRepo.save(like);

		// this add notification
		Notifications notification = new Notifications();
		notification.setSenderId(userId);
		notification.setType(Type.Like);
		notification.setCreatedAt(LocalDateTime.now());
		notification.setUserId(respRepo.findByResponseId(respId).getUserId());
		notification.setRespondId(respId);
		notificationService.createContextAndSave(notification);

		// Update the post's like count
		Responses response = respRepo.findById(respId)
				.orElseThrow(() -> new EntityNotFoundException("Response not found"));

		response.setLikes(response.getLikes() + 1);
		respRepo.save(response);
	}

	@Transactional
<<<<<<< HEAD
	public void removeRespondLike(Long responseId, Long userId) {
		if (likeRepo.existsByResponseIdAndUserId(responseId, userId)) {
	        Specification<Likes> likeSpec = Specification
	        		.where(LikeSpecifications.hasResponseId(responseId))
	                .and(LikeSpecifications.hasUserId(userId));
	        
	        Likes like = likeRepo.findOne(likeSpec)
	            .orElseThrow(() -> new EntityNotFoundException("Like not found"));
=======
	public void removeRespondLike(Long postId, Long responseId, Long userId) {
		if (likeRepo.existsByResponseIdAndPostIdAndUserId(responseId, postId, userId)) {
			Specification<Likes> likeSpec = Specification.where(LikeSpecifications.hasPostId(postId))
					.and(LikeSpecifications.hasResponseId(responseId))
					.and(LikeSpecifications.hasUserId(userId));
>>>>>>> 3e65a3c30d1437954072f1970bcb0175d129c863

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
<<<<<<< HEAD
	
	public List<Likes> getLikedPostIdsByUserId(Long userId) {
		Specification<Likes> spec = LikeSpecifications.hasUserId(userId);
		return likeRepo.findAll(spec);
	}
	
=======

>>>>>>> 3e65a3c30d1437954072f1970bcb0175d129c863
}

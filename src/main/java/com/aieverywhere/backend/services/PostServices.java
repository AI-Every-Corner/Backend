package com.aieverywhere.backend.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.aieverywhere.backend.dto.PostResponseDTO;
import com.aieverywhere.backend.models.Posts;
import com.aieverywhere.backend.repostories.ImageRepo;
import com.aieverywhere.backend.repostories.PostRepo;
import com.aieverywhere.backend.repostories.PostSpecifications;
import com.aieverywhere.backend.repostories.RelaRepo;
import com.aieverywhere.backend.repostories.UserRepo;

@Service
public class PostServices {
	private final PostRepo postRepo;
	private final UserRepo userRepo;
	private final ImageRepo imageRepo;
	private final RelaRepo relaRepo;

	@Autowired
	public PostServices(PostRepo postRepo, UserRepo userRepo, ImageRepo imageRepo, RelaRepo relaRepo) {
		this.postRepo = postRepo;
		this.userRepo = userRepo;
		this.imageRepo = imageRepo;
		this.relaRepo = relaRepo;

	}

	public Posts createPost(Posts post) {
		return postRepo.save(post);
	}

	public Posts getPostByPostId(Long postId) {
		if (!postRepo.existsById(postId)) {
			throw new RuntimeException("Post not found");
		}
		return postRepo.findByPostId(postId);
	}

	public Posts updatePost(int postId, Posts post) {
		if (!postRepo.existsByPostId(postId)) {
			throw new RuntimeException("Post not found");
		}
		return postRepo.save(post);
	}

	public void deletePost(int postId) {
		if (!postRepo.existsByPostId(postId)) {
			throw new RuntimeException("Course not found");
		}
		postRepo.deleteByPostId(postId);

	}

	public List<PostResponseDTO> getAllPosts(int page, int size) {
		Page<Posts> postsPage = postRepo.findAll(PageRequest.of(page, size));
		return postsPage.stream()
				.map(post -> new PostResponseDTO(post.getPostId(), post.getContent(),
						userRepo.findUsernameByUserId(post.getUserId()),
						imageRepo.findImagePathByImageId(post.getImgId())))
				.collect(Collectors.toList());
	}

	// the userId is for find friend status and can show friends posts first
	public List<Posts> getAllFriendPosts(Long userid) {
		List<Long> allFriends = relaRepo.findAllFriendIdByUserId(userid);
		// this will find all friends posts
		List<Posts> allpost = postRepo.findAllPostsByUserIdIn(allFriends);
		return allpost;
	}

	// search with post id ,user id, createTime ,mood tag or user name
	public List<Posts> findPostsByCriteria(Long postId, Long userId, LocalDateTime createdAt, String moodTag,
			String username) {
		Specification<Posts> spec = Specification.where(PostSpecifications.hasPostId(postId))
				.and(PostSpecifications.hasUserId(userId)).and(PostSpecifications.greaterThanCreatedAt(createdAt))
				.and(PostSpecifications.hasMoodTag(moodTag)).and(PostSpecifications.userHasName(username));
		return postRepo.findAll(spec);
	}

	// month is all upperCase English
	public Map<LocalDateTime, Double> monthReviewData(Long userId) {
		List<Posts> allUserPost = postRepo.findAllByUserIdOrderByCreatedAtAsc(userId);
		Map<LocalDateTime, List<Double> >yearReview = new HashMap<>();
		
		List<Posts> postInThisYear = allUserPost.stream()
				.filter(post -> post.getCreatedAt().getYear() == LocalDateTime.now().getYear())
				.collect(Collectors.toList());
		

		for (Posts post : postInThisYear) {
			if (yearReview.containsKey(post.getCreatedAt())) {
				List<Double> value = yearReview.get(post.getCreatedAt());
				value.add(post.getMoodScore().doubleValue());
			} else {
				yearReview.put(post.getCreatedAt(),
						new ArrayList<>(List.of(post.getMoodScore().doubleValue())));
			}
		}

		Map<LocalDateTime, Double> resultMap = new HashMap<>();
		yearReview.forEach((key, value) -> {
			if (value.size() > 1) {
				int sum = 0;
				for (int i = 0; i < value.size(); i++) {
					sum += value.get(i);
				}
				resultMap.put(key, (double) (sum / value.size()));
			} else {
				resultMap.put(key, value.get(0));
			}
		});
		return resultMap ;
	}

}

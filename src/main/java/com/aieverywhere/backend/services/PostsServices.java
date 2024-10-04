package com.aieverywhere.backend.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.aieverywhere.backend.models.Posts;
import com.aieverywhere.backend.repostories.PostRepo;
import com.aieverywhere.backend.repostories.PostSpecifications;
import com.aieverywhere.backend.repostories.RelaRepo;

@Service
public class PostsServices {
	private final PostRepo postRepo;
	private final RelaRepo relaRepo;

	@Autowired
	public PostsServices(PostRepo postRepo, RelaRepo relaRepo) {
		this.postRepo = postRepo;
		this.relaRepo = relaRepo;
	}

	public Posts createPost(Posts post) {
		return postRepo.save(post);
	}

	// the userId is for find friend status and can show friends posts first
	public List<Posts> getAllPosts(Long userid) {
		List<Long> allFriends = relaRepo.findAllFriendIdByUserId(userid);
		// this will find all friends posts
		List<Posts> allpost = postRepo.findAllPostsByUserIdIn(allFriends);
		return allpost;
	}
	
	public Posts getPostByPostId(Long postId) {
		if (!postRepo.existsById(postId)) {
			throw new RuntimeException("Post not found");
		}
		return postRepo.findByPostId(postId);
	}

	public Posts updatePost(Long postId, Posts post) {
		if (!postRepo.existsById(postId)) {
			throw new RuntimeException("Post not found");
		}
		return postRepo.save(post);
	}

	public void deletePost(Long postId) {
		if (!postRepo.existsById(postId)) {
			throw new RuntimeException("Post not found");
		}
		postRepo.deleteById(postId);
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
	public Map<Integer, Double> monthReviewData(Long userId) {
		List<Posts> allUserPost = postRepo.findAllByUserIdOrderByCreatedAtAsc(userId);
		Map<Integer, List<Double>> monthReview = new HashMap<>();

		List<Posts> postInThisMonth = allUserPost.stream()
				.filter(post -> post.getCreatedAt().getMonth() == LocalDateTime.now().getMonth())
				.collect(Collectors.toList());
		for (Posts post : postInThisMonth) {
			System.out.println(post.toString());
		}
		for (Posts post : postInThisMonth) {
			if (monthReview.containsKey(post.getCreatedAt().getDayOfMonth())) {
				List<Double> value = monthReview.get(post.getCreatedAt().getDayOfMonth());
				value.add(post.getMoodScore().doubleValue());
			} else {
				monthReview.put(post.getCreatedAt().getDayOfMonth(),
						new ArrayList<>(List.of(post.getMoodScore().doubleValue())));
			}
		}

		Map<Integer, Double> arrangeMap = new HashMap<>();
		monthReview.forEach((key, value) -> {
			if (value.size() > 1) {
				int sum = 0;
				for (int i = 0; i < value.size(); i++) {
					sum += value.get(i);
				}
				arrangeMap.put(key, (double) (sum / value.size()));
			} else {
				arrangeMap.put(key, value.get(0));
			}
		});
		return arrangeMap;
	}

}

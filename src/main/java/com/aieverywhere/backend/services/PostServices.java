package com.aieverywhere.backend.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.aieverywhere.backend.dto.PostResponseDTO;
import com.aieverywhere.backend.models.Images;
import com.aieverywhere.backend.models.Posts;
import com.aieverywhere.backend.models.Users;
import com.aieverywhere.backend.repostories.ImageRepo;
import com.aieverywhere.backend.repostories.PostRepo;
import com.aieverywhere.backend.repostories.PostSpecifications;
import com.aieverywhere.backend.repostories.RelaRepo;
import com.aieverywhere.backend.repostories.UserRepo;

import jakarta.persistence.Query;

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
	 
//	public Map<String, Object> getPaginatedPosts(int page, int size) {
//	    // Create the Pageable object with the requested page and size
//	    Pageable pageable = PageRequest.of(page, size);
//	
//	    // Create the specification
//	    Specification<Object[]> PostSpec = PostSpecifications.fetchPostDetails();
//	
//	    // Fetch paginated results from the repository
////	    Page<Object[]> paginatedPosts = postRepo.findAllPageablePosts(PostSpec, pageable);
//	    Page<Object[]> paginatedPosts = postRepo.findAll(PostSpec, pageable);
//	    System.out.println(paginatedPosts);
//	    
//	    List<PostResponseDTO> postRes = new ArrayList<>();
////		for (Object[] row : pageablePosts) {
////			Long postId = ((Number) row[0]).longValue();
////			String content = (String) row[1];
////			String username = (String) row[2];
////			String imagePath = (String) row[3];
////			postRes.add(new PostResponseDTO(postId, content, username, imagePath));
////		}
//	
//	    // Create the specification for cnt
//	    Specification<Posts> CntSpec = PostSpecifications.countPosts();
//	    Long totalItems = postRepo.count(CntSpec);
//
//		// Calculate total pages based on the total number of items and page size
//		int totalPages = (int) Math.ceil((double) totalItems / size);
//
//		// Prepare response
//		Map<String, Object> postMap = new HashMap<>();
//		postMap.put("postRes", postRes);
//		postMap.put("currentPage", page);
//		postMap.put("totalItems", totalItems);
//		postMap.put("totalPages", totalPages);
//		
//		return postMap;
//	}
	
	public Map<String, Object> getAllPosts(int page, int size) {
	    Page<Posts> postsPage = postRepo.findAll(PageRequest.of(page, size));
	    
	    List<PostResponseDTO> postsList = new ArrayList<>();
	    for (Posts post : postsPage) {
	        Long postId = post.getPostId();
	        String content = post.getContent();

	        // Fetch user information
	        Users user = userRepo.findByUserId(post.getUserId());
	        String nickname = null;
	        if (user != null) {
	            nickname = user.getNickName();
	        } else {
	            // Handle the case where the user is null
	            System.err.println("User with ID " + post.getUserId() + " not found.");
	            continue; // Skip this post if the user is not found
	        }

	        // Fetch image information
	        Images image = imageRepo.findByImageId(post.getImgId());
	        String imagePath = null;
	        if (image != null) {
	            imagePath = image.getImagePath();
	        } else {
	            System.err.println("Image with ID " + post.getImgId() + " not found.");
	        }

	        // Only add posts where both nickname and imagePath are available
	        postsList.add(new PostResponseDTO(postId, content, nickname, imagePath));
	    }

	    Map<String, Object> postMap = new HashMap<>();
	    postMap.put("postRes", postsList);
	    postMap.put("currentPage", page);
	    return postMap;
	}


//	public List<PostResponseDTO> getAllPosts(int page, int size) {
//		Page<Posts> postsPage = postRepo.findAll(PageRequest.of(page, size));
//		return postsPage.stream()
//				.map(post -> new PostResponseDTO(post.getPostId(), post.getContent(),
//						userRepo.findUsernameByUserId(post.getUserId()),
//						imageRepo.findImagePathByImageId(post.getImgId())))
//				.collect(Collectors.toList());
//	}

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

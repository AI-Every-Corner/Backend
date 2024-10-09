package com.aieverywhere.backend.services;

import java.time.LocalDate;
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
import org.springframework.web.multipart.MultipartFile;

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
	private final ImagesServices imagesServices;

	@Autowired
	public PostServices(PostRepo postRepo, UserRepo userRepo, ImageRepo imageRepo, RelaRepo relaRepo, ImagesServices imagesServices) {
		this.postRepo = postRepo;
		this.userRepo = userRepo;
		this.imageRepo = imageRepo;
		this.relaRepo = relaRepo;
		this.imagesServices = imagesServices;
	}

	public Posts createPost(Posts post, MultipartFile imageFile) {
		try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = imagesServices.uploadImage(imageFile); // 使用已經存在的 ImagesServices 來保存圖片
                Images image = new Images();
                image.setImagePath(imageUrl);
                image.setIsUploadByUser(true);
                imagesServices.createImage(image);
                post.setImageId(image.getImageId());
            }
            post.setCreatedAt(LocalDateTime.now());
            post.setUpdatedAt(LocalDateTime.now());
            return postRepo.save(post);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to store post: " + e.getMessage());
        }
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
	
	public Map<String, Object> getAllPagedPosts(int page, int size) {
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
	        Images image = imageRepo.findByImageId(post.getImageId());
	        String imagePath = null;
	        if (image != null) {
	            imagePath = image.getImagePath();
	        } else {
	            System.err.println("Image with ID " + post.getImageId() + " not found.");
	        }
	        
	        LocalDateTime updateAt = post.getUpdatedAt();
	        String location = post.getLocation();

	        // Only add posts where both nickname and imagePath are available
	        postsList.add(new PostResponseDTO(postId, content, nickname, imagePath, updateAt, location));
	    }
	    
	    Long totalItems = postRepo.count();
		int totalPages = (int) Math.ceil((double) totalItems / size);

	    Map<String, Object> postMap = new HashMap<>();
	    postMap.put("postsList", postsList);
	    postMap.put("currentPage", page);
		postMap.put("totalItems", totalItems);
		postMap.put("totalPages", totalPages);
		
	    return postMap;
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
	
	 public List<PostResponseDTO> getPostsByUserId(Long userId) {
	        // 查找所有該用戶的帖子
	        List<Posts> postsList = postRepo.findAllByUserIdOrderByCreatedAtAsc(userId);

	        List<PostResponseDTO> postResponseDTOList = new ArrayList<>();
	        for (Posts post : postsList) {
	            Users user = userRepo.findByUserId(post.getUserId());
	            Images image = imageRepo.findByImageId(post.getImageId());

	            // 構建 PostResponseDTO，檢查 user 和 image 是否為 null
	            if (user != null && image != null) {
	                postResponseDTOList.add(new PostResponseDTO(
	                    post.getPostId(),
	                    post.getContent(),
	                    user.getNickName(),
	                    image.getImagePath(),
	                    post.getUpdatedAt(),
	                    post.getLocation()

	                ));
	            }
	        }
	        return postResponseDTOList;
	    }

}

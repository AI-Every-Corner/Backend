package com.aieverywhere.backend.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.aieverywhere.backend.models.Posts;
import com.aieverywhere.backend.models.Users;
import com.aieverywhere.backend.dto.PostResponseDTO;
import com.aieverywhere.backend.services.PostServices;
import com.aieverywhere.backend.services.UsersServices;

@RestController
@RequestMapping("/")
public class PostController {

	private PostServices postServices;
	private UsersServices usersServices;

	@Autowired
	public PostController(PostServices postServices, UsersServices usersServices) {
		this.postServices = postServices;
		this.usersServices = usersServices;
	}

	@GetMapping("posts")
	public ResponseEntity<?> queryPage(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		System.out.println("in posts");

		try {
			// Prepare response
			Map<String, Object> postMap = postServices.getAllPagedPosts(page, size);
			return ResponseEntity.status(200).body(postMap);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("failed: " + e.getMessage());
		}
	}

	@GetMapping("posts/{userId}")
	public ResponseEntity<?> getPostByUserId(@PathVariable Long userId) {
		try {
			// 調用服務層的方法
			List<PostResponseDTO> posts = postServices.getPostsByUserId(userId);
			return ResponseEntity.status(200).body(posts);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("Failed to retrieve posts: " + e.getMessage());
		}
	}

	@PostMapping("yearReview/{userId}")
	public ResponseEntity<?> monthReview(@PathVariable Long userId) {
		try {
			Map<LocalDateTime, Double> month = postServices.monthReviewData(userId);
			return ResponseEntity.status(200).body(month);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("failed: " + e.getMessage());
		}

	}

	@PostMapping("createPost")
	public ResponseEntity<?> createPost(
			@RequestPart("post") Posts post,
			@RequestPart(value = "image", required = false) MultipartFile imageFile) {
		try {
			postServices.createPost(post, imageFile);
			return ResponseEntity.status(201).body("Post created successfully");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("Failed to create post: " + e.getMessage());
		}
	}

	@GetMapping("search")
	public ResponseEntity<?> getPostsOrFriendsByContext(
			@RequestParam(value = "searchcontext", required = false) String searchContent,
			@RequestParam(value = "post", required = false) String post,
			@RequestParam(value = "user", required = false) String user) {

		try {
			if (user == "") {
				List<Posts> posts = postServices.searchPostsByContent(searchContent);
				return ResponseEntity.status(200).body(posts);

			} else {
				List<Users> users = usersServices.searchAndRemoveDuplicates(searchContent);
				return ResponseEntity.status(200).body(users);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("Failed to retrieve posts: " +
					e.getMessage());

		}

	}

}

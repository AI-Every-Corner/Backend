package com.aieverywhere.backend.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.aieverywhere.backend.dto.PostResponseDTO;
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

    public List<PostResponseDTO> getAllPosts(int page, int size) {
        Page<Posts> postsPage = postRepo.findAll(PageRequest.of(page, size));
        return postsPage.stream()
                .map(post -> new PostResponseDTO(post.getPostId(), post.getContent(), post.getUser().getUsername()))
                .collect(Collectors.toList());
    }
	
	
	
	
	
	
}

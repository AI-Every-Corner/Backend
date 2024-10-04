package com.aieverywhere.backend.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aieverywhere.backend.models.Responses;
import com.aieverywhere.backend.repostories.RespRepo;

@Service
public class ResponsesServices {

    @Autowired
    private RespRepo respRepo;

    // Create a new response
    public Responses createResponse(Responses response) {
        response.setCreatedAt(LocalDateTime.now());
        response.setUpdateAt(LocalDateTime.now());
        return respRepo.save(response);
    }

    // Read all responses
    public List<Responses> getAllResponses() {
        return respRepo.findAll();
    }
    
 // get all responses by postId
    public List<Responses> getAllResponsesByPostId(Long postId) {
        return respRepo.findAllByPostId(postId);
    }

    // Read a single response by ID
    public Responses getResponseById(Long id) {
        return respRepo.findByResponseId(id);
    }

    // Update an existing response
    public Responses updateResponse(Long id, Responses responseDetails) {
        Responses response = respRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Response not found"));
        
        response.setContent(responseDetails.getContent());
        response.setPostId(responseDetails.getPostId());
        response.setUserId(responseDetails.getUserId());
        response.setUpdateAt(LocalDateTime.now());
        
        return respRepo.save(response);
    }

    // Delete a response
    public void deleteResponse(Long id) {
        respRepo.deleteById(id);
    }
}

package com.aieverywhere.backend.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aieverywhere.backend.dto.FriendsDTO;
import com.aieverywhere.backend.services.RelationshipServices;

@RestController
@RequestMapping("/")
public class RelationshipController {

    @Autowired
    private RelationshipServices relationshipServices;

    @GetMapping("/friends/{userId}")
    public ResponseEntity<?> getFriendesList(@PathVariable Long userId){
        try{
            List<FriendsDTO> friendesList = relationshipServices.getFriendesList(userId);
            return ResponseEntity.ok(friendesList);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("獲取好友列表失敗: " + e.getMessage());
        }
    }
}

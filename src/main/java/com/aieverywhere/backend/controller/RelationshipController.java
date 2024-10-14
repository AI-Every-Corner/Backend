package com.aieverywhere.backend.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aieverywhere.backend.dto.FriendsDTO;
import com.aieverywhere.backend.models.Relationship;
import com.aieverywhere.backend.services.RelationshipServices;

@RestController
@RequestMapping("/")
public class RelationshipController {

    @Autowired
    private RelationshipServices relationshipServices;

    @GetMapping("/friends/{userId}")
    public ResponseEntity<?> getFriendesList(@PathVariable Long userId) {
        try {
            List<FriendsDTO> friendesList = relationshipServices.getFriendesList(userId);
            return ResponseEntity.ok(friendesList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("獲取好友列表失敗: " + e.getMessage());
        }
    }

    @PostMapping("/{userId}/follow/{friendId}")
    public ResponseEntity<?> createFollowRelationship(@PathVariable Long userId, @PathVariable Long friendId) {
        try {
            Relationship relationship = new Relationship();
            relationship.setUserId(userId);
            relationship.setFriendId(friendId);
            relationship.setRelationshipStatus(Relationship.RelationshipStatus.Friend);
            relationshipServices.createFollowRelationship(relationship);
            return ResponseEntity.ok("追蹤成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("追蹤失敗: " + e.getMessage());
        }
    }

    @GetMapping("/{userId}/checkfollow/{friendId}")
    public ResponseEntity<?> checkFollowStatus(@PathVariable Long userId, @PathVariable Long friendId) {
        try {
            Boolean isFollowing = relationshipServices.checkFollowRelationship(userId, friendId);
            return ResponseEntity.ok(isFollowing);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("檢查追蹤狀態失敗：" + e.getMessage());
        }
    }

    @DeleteMapping("/{userId}/unfollow/{friendId}")
    public ResponseEntity<String> unfollow(@PathVariable Long userId, @PathVariable Long friendId) {
        if (relationshipServices.checkFollowRelationship(userId, friendId)) {
            relationshipServices.deleteRelationship(userId, friendId);
            return ResponseEntity.ok("Unfollowed successfully");
        } else {
            return ResponseEntity.badRequest().body("Not following this user");
        }
    }

}

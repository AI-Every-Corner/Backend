package com.aieverywhere.backend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Relationship {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long relationshipId;
	private Long userId;
	private Long friendId;
	@Enumerated(EnumType.STRING)
	private RelationshipStatus relationshipStatus;

	public Relationship() {

	}

	public Relationship(Long relationshipId, Long userId, Long friendId, RelationshipStatus relationshipStatus) {
		this.relationshipId = relationshipId;
		this.userId = userId;
		this.friendId = friendId;
		this.relationshipStatus = relationshipStatus;
	}

	public Long getRelationshipId() {
		return relationshipId;
	}

	public void setRelationshipId(Long relationshipId) {
		this.relationshipId = relationshipId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getFriendId() {
		return friendId;
	}

	public void setFriendId(Long friendId) {
		this.friendId = friendId;
	}

	public RelationshipStatus getRelationshipStatus() {
		return relationshipStatus;
	}

	public void setRelationshipStatus(RelationshipStatus relationshipStatus) {
		this.relationshipStatus = relationshipStatus;
	}

	public enum RelationshipStatus {
		Bestie, Friend
	}
}

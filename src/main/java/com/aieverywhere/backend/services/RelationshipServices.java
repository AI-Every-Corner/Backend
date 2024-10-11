package com.aieverywhere.backend.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aieverywhere.backend.dto.FriendsDTO;
import com.aieverywhere.backend.models.Relationship;
import com.aieverywhere.backend.models.Relationship.RelationshipStatus;
import com.aieverywhere.backend.models.Users;
import com.aieverywhere.backend.models.Users.Role;
import com.aieverywhere.backend.repostories.RelaRepo;
@Service
public class RelationshipServices {
	private final RelaRepo relaRepo;

	@Autowired
	private UsersServices usersServices;
	@Autowired
	public RelationshipServices(RelaRepo relaRepo) {
		this.relaRepo = relaRepo;
	}

	// create two relationship for user and friend at same time
	public String createRelationship(Relationship relationship) {
		// a friend b mean b friend a
		relaRepo.save(relationship);
		Long relaId = relaRepo.findRelationshipIdByUserIdAndFriendId(relationship.getUserId(),
				relationship.getFriendId());

		Relationship createforfriend = new Relationship(relaId + 1, relationship.getFriendId(),
				relationship.getUserId(), relationship.getRelationshipStatus());
		relaRepo.save(createforfriend);
		return "create success";
	}

	// get all user friends relationship
	public List<Relationship> getAllUserFriends(Long UserId) {
		List<Relationship> allFriends = relaRepo.findAllByUserId(UserId);

		return allFriends;
	}

	// get all user friends relationship and can choose with role
	public List<Relationship> getAllUserFriendsWithRole(Long UserId, Role role) {
		List<Relationship> allFriendsWithRole = relaRepo.findAllByUserIdAndRole(UserId, role);

		return allFriendsWithRole;
	}

	// delete two relationship for user and friend at same time
	public void deleteRelationship(Relationship relationship) {
		Long relaId = relaRepo.findRelationshipIdByUserIdAndFriendId(relationship.getUserId(),
				relationship.getFriendId());
		relaRepo.deleteByRelationshipId(relaId);
		relaRepo.deleteByRelationshipId(relaId + 1);

	}

	// update two relationship for user and friend at same time
	public String updateRelationship(Relationship relationship) {
		Long relaId = relaRepo.findRelationshipIdByUserIdAndFriendId(relationship.getUserId(),
				relationship.getFriendId());
		Relationship createforfriend = new Relationship(relaId + 1, relationship.getFriendId(),
				relationship.getUserId(), relationship.getRelationshipStatus());
		relaRepo.save(createforfriend);
		return "update success";
	}

	// check relationship
	public Boolean checkRelationship(Long userId, Long friendId) {
		System.out.println(userId+" "+friendId);
		if (relaRepo.findRelationshipIdByUserIdAndFriendId(userId, friendId) != null) {
			return true;
		} else {
			return false;
		}
	}

	public List<FriendsDTO> getFriendesList(Long userId){

		// get all user friends relationship
		List<Relationship> relationships = relaRepo.findAllByUserIdAndRelationshipStatus(userId, RelationshipStatus.Friend);

		// get all user friends information
		return relationships.stream()
            .map(relationship -> {
				try {
					Users friend = usersServices.getUsersByUsersId(relationship.getFriendId());
					return new FriendsDTO(
                    friend.getUserId(),
                    friend.getNickName(),
                    friend.getImagePath(),
                    relationship.getFriendId()
                );
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
                
            })
            .collect(Collectors.toList());
	}
}
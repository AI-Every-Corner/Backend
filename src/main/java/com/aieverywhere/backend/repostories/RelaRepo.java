package com.aieverywhere.backend.repostories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aieverywhere.backend.models.Relationship;
import com.aieverywhere.backend.models.Relationship.RelationshipStatus;
import com.aieverywhere.backend.models.Users.Role;

public interface RelaRepo extends JpaRepository<Relationship, Integer>, JpaSpecificationExecutor<Relationship> {

    void deleteByRelationshipId(Long relationshipId);

    @Query("SELECT r.relationshipId FROM Relationship r WHERE r.userId = ?1 AND r.friendId = ?2")
    Long findRelationshipIdByUserIdAndFriendId(Long userId, Long friendId);

    List<Relationship> findAllByUserId(Long userId);

    List<Long> findAllFriendIdByUserId(Long userId);

    @Query("SELECT r FROM Relationship r JOIN Users u ON r.friendId = u.userId WHERE r.userId = :userId AND u.role = :role")
    List<Relationship> findAllByUserIdAndRole(@Param("userId") Long userId, @Param("role") Role role);

    List<Relationship> findAllByUserIdAndRelationshipStatus(Long userId, RelationshipStatus status);

    List<Relationship> findAllByFriendId(Long userId);

}
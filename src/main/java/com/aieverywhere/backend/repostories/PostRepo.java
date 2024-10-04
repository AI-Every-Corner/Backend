package com.aieverywhere.backend.repostories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aieverywhere.backend.models.Posts;


@Repository
public interface PostRepo extends JpaRepository<Posts, Integer>, JpaSpecificationExecutor<Posts> {

}

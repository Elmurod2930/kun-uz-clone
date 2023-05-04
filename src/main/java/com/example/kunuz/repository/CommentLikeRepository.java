package com.example.kunuz.repository;


import com.example.kunuz.entity.CommentLikeEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentLikeRepository extends CrudRepository<CommentLikeEntity, Integer> {
    @Query("from CommentLikeEntity where commentId=:commentId and profileId=:profileId")
    Optional<CommentLikeEntity> get(@Param("commentId") Integer commentId, @Param("profileId") Integer profileId);
}

package com.example.kunuz.repository;

import com.example.kunuz.entity.CommentEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends CrudRepository<CommentEntity, Integer>,
        PagingAndSortingRepository<CommentEntity, Integer> {
    @Query("from CommentEntity where articleId=:id and visible=true")
    List<CommentEntity> getCommentByArticleId(@Param("id") String id);

    @Query("from CommentEntity where replyId=:id and visible=true")
    List<CommentEntity> getReplyListByCommentId(@Param("id") String commentId);

}

package com.example.kunuz.service;

import com.example.kunuz.entity.CategoryEntity;
import com.example.kunuz.entity.CommentEntity;
import com.example.kunuz.entity.CommentLikeEntity;
import com.example.kunuz.entity.ProfileEntity;
import com.example.kunuz.enums.LikeStatus;
import com.example.kunuz.exps.ItemNotFoundException;
import com.example.kunuz.repository.CommentLikeRepository;
import com.example.kunuz.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentLikeService {
    private final CommentLikeRepository commentLikeRepository;
    private final CommentService commentService;
    private final CommentRepository commentRepository;

    public Boolean like(Integer commentId, Integer profileId) {
        CommentEntity comment = commentService.get(commentId);
        CommentLikeEntity entity = get(commentId, profileId);
        if (entity == null) {
            entity = new CommentLikeEntity();
            entity.setStatus(LikeStatus.LIKE);
            entity.setProfileId(profileId);
            entity.setCommentId(commentId);
            commentLikeRepository.save(entity);
            return true;
        }
        if (entity.getStatus().equals(LikeStatus.LIKE)) {
            commentLikeRepository.delete(entity);
            comment.setLikeCount(comment.getLikeCount() - 1);
        } else {
            commentLikeRepository.delete(entity);
            entity.setId(null);
            entity.setStatus(LikeStatus.LIKE);
            commentLikeRepository.save(entity);
            comment.setDisLikeCount(comment.getDisLikeCount() - 1);
            comment.setLikeCount(comment.getLikeCount() + 1);
        }
        return true;
    }

    public Boolean disLike(Integer commentId, Integer profileId) {
        CommentEntity comment = commentService.get(commentId);
        CommentLikeEntity entity = get(commentId, profileId);
        if (entity == null) {
            comment.setDisLikeCount(comment.getDisLikeCount() + 1);
            entity = new CommentLikeEntity();
            entity.setCommentId(commentId);
            entity.setProfileId(profileId);
            entity.setStatus(LikeStatus.DISLIKE);
            commentLikeRepository.save(entity);
            return true;
        }
        if (entity.getStatus().equals(LikeStatus.LIKE)) {
            comment.setDisLikeCount(comment.getDisLikeCount() + 1);
            comment.setLikeCount(comment.getLikeCount() - 1);
            commentLikeRepository.delete(entity);
            entity.setId(null);
            entity.setStatus(LikeStatus.DISLIKE);
            commentLikeRepository.save(entity);
        } else {
            comment.setDisLikeCount(comment.getDisLikeCount() - 1);
            commentLikeRepository.delete(entity);
        }
        return true;
    }

    public CommentLikeEntity get(Integer commentId, Integer profileId) {
        Optional<CommentLikeEntity> entity = commentLikeRepository.get(commentId, profileId);
        return entity.orElse(null);
    }

    public Boolean remove(Integer commentId, Integer profileId) {
        CommentLikeEntity isCommentLikeEntity = get(commentId, profileId);
        CommentEntity comment = commentService.get(commentId);
        comment.setLikeCount(comment.getLikeCount() - 1);
        commentRepository.save(comment);
        return true;
    }
}

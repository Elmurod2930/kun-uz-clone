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
    private final ProfileService profileService;
    private final CommentRepository commentRepository;

    public Boolean like(Integer commentId, Integer profileId) {
        ProfileEntity profile = profileService.get(profileId);
        CommentEntity comment = commentService.get(commentId);
        CommentLikeEntity entity = new CommentLikeEntity();
        entity.setComment(comment);
        entity.setProfile(profile);
        entity.setStatus(LikeStatus.LIKE);
        comment.setLikeCount(comment.getLikeCount() + 1);
        commentLikeRepository.save(entity);
        commentRepository.save(comment);
        return true;
    }

    public Boolean disLike(Integer commentId, Integer profileId) {
        CommentEntity comment = commentService.get(commentId);
        CommentLikeEntity entity = get(commentId, profileId);
        if (entity == null) {
            comment.setLikeCount(comment.getLikeCount() - 1);
        }
        comment.setDisLikeCount(comment.getDisLikeCount() + 1);
        commentRepository.save(comment);
        entity.setComment(comment);
        entity.setProfileId(profileId);
        entity.setStatus(LikeStatus.DISLIKE);
        commentLikeRepository.save(entity);
        return true;
    }

    public CommentLikeEntity get(Integer commentId, Integer profileId) {
        Optional<CommentLikeEntity> entity = commentLikeRepository.get(commentId, profileId);
        if (entity.isEmpty()) {
            throw new ItemNotFoundException("not fount comment like");
        }
        return entity.get();
    }

    public Boolean remove(Integer commentId, Integer profileId) {
        CommentLikeEntity isCommentLikeEntity = get(commentId, profileId);
        CommentEntity comment = commentService.get(commentId);
        comment.setLikeCount(comment.getLikeCount() - 1);
        commentRepository.save(comment);
        return true;
    }
}

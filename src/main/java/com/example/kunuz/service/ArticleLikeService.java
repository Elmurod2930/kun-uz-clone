package com.example.kunuz.service;

import com.example.kunuz.entity.*;
import com.example.kunuz.enums.LikeStatus;
import com.example.kunuz.repository.ArticleLikeRepository;
import com.example.kunuz.repository.ArticleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ArticleLikeService {
    private final ArticleService articleService;
    private final ArticleRepository articleRepository;
    private final ArticleLikeRepository articleLikeRepository;

    public Boolean like(String articleId, Integer profileId) {
        ArticleEntity comment = articleService.get(articleId);
        ArticleLikeEntity entity = get(articleId, profileId);
        if (entity == null) {
            entity = new ArticleLikeEntity();
            entity.setStatus(LikeStatus.LIKE);
            entity.setProfileId(profileId);
            entity.setArticleId(articleId);
            articleLikeRepository.save(entity);
            return true;
        }
        if (entity.getStatus().equals(LikeStatus.LIKE)) {
            articleLikeRepository.delete(entity);
            comment.setLikeCount(comment.getLikeCount() - 1);
        } else {
            articleLikeRepository.delete(entity);
            entity.setId(null);
            entity.setStatus(LikeStatus.LIKE);
            articleLikeRepository.save(entity);
            comment.setDisLikeCount(comment.getDisLikeCount() - 1);
            comment.setLikeCount(comment.getLikeCount() + 1);
        }
        return true;
    }

    public Boolean disLike(String articleId, Integer profileId) {
        ArticleEntity comment = articleService.get(articleId);
        ArticleLikeEntity entity = get(articleId, profileId);
        if (entity == null) {
            comment.setDisLikeCount(comment.getDisLikeCount() + 1);
            entity = new ArticleLikeEntity();
            entity.setArticleId(articleId);
            entity.setProfileId(profileId);
            entity.setStatus(LikeStatus.DISLIKE);
            articleLikeRepository.save(entity);
            return true;
        }
        if (entity.getStatus().equals(LikeStatus.LIKE)) {
            comment.setDisLikeCount(comment.getDisLikeCount() + 1);
            comment.setLikeCount(comment.getLikeCount() - 1);
            articleLikeRepository.delete(entity);
            entity.setId(null);
            entity.setStatus(LikeStatus.DISLIKE);
            articleLikeRepository.save(entity);
        } else {
            comment.setDisLikeCount(comment.getDisLikeCount() - 1);
            articleLikeRepository.delete(entity);
        }
        return true;
    }

    public ArticleLikeEntity get(String articleId, Integer profileId) {
        Optional<ArticleLikeEntity> entity = articleLikeRepository.get(articleId, profileId);
        return entity.orElse(null);
    }

    public Boolean remove(String articleId, Integer profileId) {
        ArticleLikeEntity isCommentLikeEntity = get(articleId, profileId);
        ArticleEntity comment = articleService.get(articleId);
        comment.setLikeCount(comment.getLikeCount() - 1);
        articleRepository.save(comment);
        return true;
    }
}

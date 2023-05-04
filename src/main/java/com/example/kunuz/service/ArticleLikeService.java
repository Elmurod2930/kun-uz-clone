package com.example.kunuz.service;

import com.example.kunuz.entity.ArticleEntity;
import com.example.kunuz.entity.ArticleLikeEntity;
import com.example.kunuz.entity.ProfileEntity;
import com.example.kunuz.enums.LikeStatus;
import com.example.kunuz.exps.ItemNotFoundException;
import com.example.kunuz.repository.ArticleLikeRepository;
import com.example.kunuz.repository.ArticleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@AllArgsConstructor
public class ArticleLikeService {
    private final ProfileService profileService;
    private final ArticleService articleService;
    private final ArticleRepository articleRepository;
    private final ArticleLikeRepository articleLikeRepository;
    public Boolean like(String articleId, Integer profileId) {
        ProfileEntity profile = profileService.get(profileId);
        ArticleEntity article = articleService.get(articleId);
        ArticleLikeEntity entity = new ArticleLikeEntity();
        entity.setArticle(article);
        entity.setProfile(profile);
        entity.setStatus(LikeStatus.LIKE);
        article.setLikeCount(article.getLikeCount() + 1);
        articleLikeRepository.save(entity);
        articleRepository.save(article);
        return true;
    }

    public Boolean disLike(String articleId, Integer profileId) {
        ArticleEntity article = articleService.get(articleId);
        ArticleLikeEntity entity = get(articleId, profileId);
        if (entity == null) {
            article.setLikeCount(article.getLikeCount() - 1);
        }
        article.setDisLikeCount(article.getDisLikeCount() + 1);
        articleRepository.save(article);
        entity.setArticle(article);
        entity.setProfileId(profileId);
        entity.setStatus(LikeStatus.DISLIKE);
        articleLikeRepository.save(entity);
        return true;
    }

    public ArticleLikeEntity get(String articleId, Integer profileId) {
        Optional<ArticleLikeEntity> entity = articleLikeRepository.get(articleId, profileId);
        if (entity.isEmpty()) {
            throw new ItemNotFoundException("not fount article like");
        }
        return entity.get();
    }

    public Boolean remove(String articleId, Integer profileId) {
        ArticleLikeEntity articleLikeEntity = get(articleId, profileId);
        ArticleEntity article = articleService.get(articleId);
        article.setLikeCount(article.getLikeCount() - 1);
        articleRepository.save(article);
        return true;
    }
}

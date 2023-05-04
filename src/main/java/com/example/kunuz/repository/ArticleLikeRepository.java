package com.example.kunuz.repository;

import com.example.kunuz.entity.ArticleLikeEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ArticleLikeRepository extends CrudRepository<ArticleLikeEntity,Integer> {
    @Query("from ArticleLikeEntity where articleId=:articleId and profileId=:profileId")
    Optional<ArticleLikeEntity> get(String articleId, Integer profileId);
}

package com.example.kunuz.repository;

import com.example.kunuz.dto.article.SavedArticleResponseDTO;
import com.example.kunuz.entity.SavedArticleEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SavedArticleRepository extends CrudRepository<SavedArticleEntity, Integer> {
    @Query("update SavedArticleEntity set visible = false where profileId=:profileId and id=:id")
    boolean deleteById(@Param("id") Integer id, @Param("profileId") Integer profileId);

    @Query("select s.id,s.article from SavedArticleEntity s where s.profileId=:id")
    List<SavedArticleResponseDTO> getList(@Param("id") Integer profileId);
}

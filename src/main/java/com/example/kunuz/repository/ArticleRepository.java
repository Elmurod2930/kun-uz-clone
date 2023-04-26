package com.example.kunuz.repository;

import com.example.kunuz.entity.ArticleEntity;
import com.example.kunuz.entity.ArticleTypeEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends CrudRepository<ArticleEntity, String> {
    @Query(value = "select a from ArticleEntity a where type=:typeEntity order by createdDate limit 3", nativeQuery = true)
    List<ArticleEntity> findAllByType(@Param("typeEntity") ArticleTypeEntity typeEntity);

    @Query(value = "select a from ArticleEntity a where id not in :idList order by createdDate desc limit 8 ", nativeQuery = true)
    List<ArticleEntity> articleShortInfo(@Param("idList") List<Integer> idList);
}

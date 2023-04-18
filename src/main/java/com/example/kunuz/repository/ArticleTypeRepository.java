package com.example.kunuz.repository;

import com.example.kunuz.entity.ArticleTypeEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ArticleTypeRepository extends CrudRepository<ArticleTypeEntity, Integer>,
        PagingAndSortingRepository<ArticleTypeEntity, Integer> {
    @Query("select id,nameEn from ArticleTypeEntity where visible=true ")
    List<ArticleTypeEntity> findByNameEng();

    @Query("select id,nameRu from ArticleTypeEntity where visible=true ")
    List<ArticleTypeEntity> findByNameRu();
    @Query("select id,nameUz from ArticleTypeEntity where visible=true ")
    List<ArticleTypeEntity> findByNameUz();
}

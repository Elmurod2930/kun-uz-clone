package com.example.kunuz.repository;

import com.example.kunuz.entity.ArticleEntity;
import com.example.kunuz.entity.ArticleTypeEntity;
import com.example.kunuz.entity.CategoryEntity;
import com.example.kunuz.entity.RegionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import javax.swing.event.ListDataEvent;
import java.util.List;

public interface ArticleRepository extends CrudRepository<ArticleEntity, String>,
        PagingAndSortingRepository<ArticleEntity, String> {
    @Query(value = "select a from ArticleEntity a where type=:typeEntity order by createdDate limit 3", nativeQuery = true)
    List<ArticleEntity> findAllByType(@Param("typeEntity") ArticleTypeEntity typeEntity);

    @Query(value = "select a from ArticleEntity a where id not in :idList order by createdDate desc limit 8 ", nativeQuery = true)
    List<ArticleEntity> articleShortInfo(@Param("idList") List<Integer> idList);

    @Query(value = "select a from ArticleEntity a order by a.viewCount limit 4", nativeQuery = true)
    List<ArticleEntity> mostReadArticles();

    @Query("select a from ArticleEntity a where a.type=:type and a.region=:region and a.visible=true order by a.createdDate limit 5")
    List<ArticleEntity> findByTypeAndRegion(ArticleTypeEntity type, RegionEntity region);

    Page<ArticleEntity> findByRegionId(Integer region_id, Pageable pageable);

    @Query("select a from ArticleEntity a where a.category=:category")
    List<ArticleEntity> get5CategoryArticle(@Param("category") CategoryEntity category);

    Page<ArticleEntity> findByCategoryId(Integer id, Pageable pageable);

    @Query("select a from ArticleEntity a where a.type=:type and a.id <>:id and a.visible=true order by a.createdDate desc limit 4")
    List<ArticleEntity> findByTypeIdAndIdNot(@Param("type") ArticleTypeEntity type, @Param("id") String id);
}

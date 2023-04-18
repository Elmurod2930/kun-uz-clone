package com.example.kunuz.repository;

import com.example.kunuz.entity.CategoryEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface CategoryRepository extends CrudRepository<CategoryEntity, Integer>,
        PagingAndSortingRepository<CategoryEntity, Integer> {
    @Query("select id,nameEn from CategoryEntity ")
    List<CategoryEntity> findByNameEn();

    @Query("select id,nameRu from CategoryEntity ")
    List<CategoryEntity> findByNameRu();

    @Query("select id,nameUz from CategoryEntity ")
    List<CategoryEntity> findByNameUz();
}

package com.example.kunuz.repository;

import com.example.kunuz.entity.RegionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface RegionRepository extends CrudRepository<RegionEntity, Integer>,
        PagingAndSortingRepository<RegionEntity, Integer> {
    @Query(" from RegionEntity where visible = true")
    List<RegionEntity> getAll();

    @Query("select id,nameEn from RegionEntity where visible=true ")
    List<RegionEntity> findByNameEng();

    @Query("select id,nameRu from RegionEntity where visible=true ")
    List<RegionEntity> findByNameRu();

    @Query("select id,nameUz from RegionEntity where visible=true ")
    List<RegionEntity> findByNameUz();
}

package com.example.kunuz.repository;

import com.example.kunuz.entity.ProfileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ProfileRepository extends CrudRepository<ProfileEntity, Integer>,
        PagingAndSortingRepository<ProfileEntity, Integer> {
    Optional<ProfileEntity> findByEmailAndPasswordAndVisible(String login, String md5Hash, boolean visible);

    Optional<ProfileEntity> findById(Integer id);

    Page<ProfileEntity> findAll(Pageable paging);

    Optional<ProfileEntity> findByEmail(String email);
}

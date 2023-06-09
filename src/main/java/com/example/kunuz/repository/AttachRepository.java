package com.example.kunuz.repository;

import com.example.kunuz.entity.AttachEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AttachRepository extends CrudRepository<AttachEntity, String>,
        PagingAndSortingRepository<AttachEntity, String> {

}

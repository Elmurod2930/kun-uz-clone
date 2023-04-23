package com.example.kunuz.repository;

import com.example.kunuz.entity.EmailHistoryEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EmailHistoryRepository extends CrudRepository<EmailHistoryEntity, Integer>,
        PagingAndSortingRepository<EmailHistoryEntity, Integer> {
    List<EmailHistoryEntity> findAllByEmail(String email);

    @Query(value = "select e from EmailHistoryEntity e where email=:email and created_date>=:dateFrom and created_date <= :dateTo", nativeQuery = true)
    List<EmailHistoryEntity> findByEmailAndDate(@Param("email") String email, @Param("dateFrom") LocalDateTime dateFrom, @Param("dateTo") LocalDateTime dateTo);
}

package com.example.kunuz.repository;

import com.example.kunuz.dto.profile.ProfileFilterRequestDTO;
import com.example.kunuz.entity.ProfileEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProfileCustomRepository {
    @Autowired
    private EntityManager entityManager;

    public List<ProfileEntity> filter(ProfileFilterRequestDTO filterRequestDTO) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder builder = new StringBuilder();
        builder.append("Select p From ProfileEntity as p where visible = true");
        if (filterRequestDTO.getName() != null) {
            params.put("name", filterRequestDTO.getName());
            builder.append(" and p.name =:name");
        }
        if (filterRequestDTO.getSurname() != null) {
            params.put("surname", filterRequestDTO.getSurname());
            builder.append(" and p.surname =:surname");
        }
        if (filterRequestDTO.getRole() != null) {
            params.put("role", filterRequestDTO.getRole());
            builder.append(" and p.role =:role");
        }
        if (filterRequestDTO.getCreatedDateFrom() != null && filterRequestDTO.getCreatedDateTo() != null) {
            builder.append(" and p.createdDate between :dateFrom and :dateTo ");
            params.put("dateFrom", LocalDateTime.of(filterRequestDTO.getCreatedDateFrom(), LocalTime.MIN));
            params.put("dateTo", LocalDateTime.of(filterRequestDTO.getCreatedDateTo(), LocalTime.MAX));
        } else if (filterRequestDTO.getCreatedDateFrom() != null) {
            builder.append(" and p.createdDate >= :dateFrom ");
            params.put("dateFrom", LocalDateTime.of(filterRequestDTO.getCreatedDateFrom(), LocalTime.MIN));
        } else if (filterRequestDTO.getCreatedDateTo() != null) {
            builder.append(" and p.createdDate <= :dateTo ");
            params.put("dateTo", LocalDateTime.of(filterRequestDTO.getCreatedDateTo(), LocalTime.MIN));
        }
        Query query = this.entityManager.createQuery(builder.toString());
        for (Map.Entry<String, Object> param : params.entrySet()) {
            query.setParameter(param.getKey(), param.getValue());
        }

        List profileList = query.getResultList();
        return profileList;
    }
    //name,surname,phone,role,created_date_from,created_date_to
}

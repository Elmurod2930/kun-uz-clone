package com.example.kunuz.service;

import com.example.kunuz.dto.email.EmailHistoryDTO;
import com.example.kunuz.entity.EmailHistoryEntity;
import com.example.kunuz.exps.AppBadRequestException;
import com.example.kunuz.repository.EmailHistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

@Service
@AllArgsConstructor
public class EmailHistoryService {
    private final EmailHistoryRepository emailHistoryRepository;

    public List<EmailHistoryDTO> getHistory(String email) {
        List<EmailHistoryEntity> entityList = emailHistoryRepository.findAllByEmail(email);
        List<EmailHistoryDTO> dtoList = new LinkedList<>();
        for (EmailHistoryEntity entity : entityList) {
            dtoList.add(entityToDTO(entity));
        }
        return dtoList;
    }

    public EmailHistoryDTO entityToDTO(EmailHistoryEntity entity) {
        EmailHistoryDTO dto = new EmailHistoryDTO();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setMassage(entity.getMassage());
        return dto;
    }

    public List<EmailHistoryDTO> getHistory(String email, LocalDate date) {
        if (email == null) {
            throw new AppBadRequestException("invalid email");
        }
        LocalDateTime dateFrom = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime dateTo = LocalDateTime.of(date, LocalTime.MAX);
        List<EmailHistoryEntity> entityList = emailHistoryRepository.findByEmailAndDate(email, dateFrom, dateTo);
        List<EmailHistoryDTO> dtoList = new LinkedList<>();
        for (EmailHistoryEntity entity : entityList) {
            dtoList.add(entityToDTO(entity));
        }
        return dtoList;
    }

    public Page<EmailHistoryDTO> pagination(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<EmailHistoryEntity> entityPage = emailHistoryRepository.findAll(pageable);
        long totalCount = entityPage.getTotalElements();
        List<EmailHistoryEntity> entityList = entityPage.getContent();
        List<EmailHistoryDTO> dtoList = new LinkedList<>();
        for (EmailHistoryEntity entity : entityList) {
            dtoList.add(entityToDTO(entity));
        }
        return new PageImpl<>(dtoList, pageable, totalCount);
    }
}

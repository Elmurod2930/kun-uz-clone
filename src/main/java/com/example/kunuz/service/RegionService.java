package com.example.kunuz.service;

import com.example.kunuz.dto.RegionDTO;
import com.example.kunuz.entity.ArticleTypeEntity;
import com.example.kunuz.entity.RegionEntity;
import com.example.kunuz.exps.AppBadRequestException;
import com.example.kunuz.exps.RegionNotFoundException;
import com.example.kunuz.repository.RegionRepository;
import jakarta.persistence.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


@Service
public class RegionService {
    @Autowired
    private RegionRepository regionRepository;

    public RegionDTO create(RegionDTO dto) {
        isValidRegion(dto);
        RegionEntity entity = new RegionEntity();
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        regionRepository.save(entity);
        return dto;
    }

    public void isValidRegion(RegionDTO dto) {
        if (dto.getNameUz().isEmpty() || dto.getNameUz().isBlank()) {
            throw new AppBadRequestException("invalid name uz");
        }
        if (dto.getNameEn().isEmpty() || dto.getNameEn().isBlank()) {
            throw new AppBadRequestException("invalid name en");
        }
        if (dto.getNameRu().isEmpty() || dto.getNameRu().isBlank()) {
            throw new AppBadRequestException("invalid name ru");
        }
    }

    public RegionDTO update(RegionDTO dto, Integer id) {
        Optional<RegionEntity> optional = regionRepository.findById(id);
        if (optional.isPresent()) {
            RegionEntity entity = new RegionEntity();
            if (dto.getNameRu() != null) {
                entity.setNameRu(dto.getNameRu());
            }
            if (dto.getNameEn() != null) {
                entity.setNameEn(dto.getNameEn());
            }
            if (dto.getNameUz() != null) {
                entity.setNameUz(dto.getNameUz());
            }
            regionRepository.save(entity);
            return dto;
        }
        throw new RegionNotFoundException("not found region");
    }

    public Boolean deleteById(Integer id) {
        Optional<RegionEntity> optional = regionRepository.findById(id);
        if (optional.isPresent()) {
            RegionEntity entity = optional.get();
            entity.setVisible(false);
            regionRepository.save(entity);
            return true;
        }
        throw new RegionNotFoundException("region noy found exception");
    }

    public List<RegionDTO> getAll() {
        Iterable<RegionEntity> entityIterable = regionRepository.findAll();
        List<RegionDTO> dtoList = new LinkedList<>();
        for (RegionEntity entity : entityIterable) {
            dtoList.add(entityToDTO(entity));
        }
        return dtoList;
    }

    public RegionDTO entityToDTO(RegionEntity entity) {
        RegionDTO dto = new RegionDTO();
        dto.setId(entity.getId());
        dto.setNameUz(entity.getNameUz());
        dto.setNameEn(entity.getNameEn());
        dto.setNameRu(entity.getNameRu());
        return dto;
    }

    public List<RegionDTO> getByLang(String lang) {
        List<RegionEntity> entityList = null;
        if (lang.equals("en")) {
            entityList = regionRepository.findByNameEng();
        } else if (lang.equals("ru")) {
            entityList = regionRepository.findByNameRu();
        } else if (lang.equals("uz")) {
            entityList = regionRepository.findByNameUz();
        } else {
            throw new AppBadRequestException("not found '" + lang + "'");
        }
        List<RegionDTO> dtoList = new LinkedList<>();
        for (RegionEntity entity : entityList) {
            dtoList.add(entityToDTO(entity));
        }
        return dtoList;
    }
}

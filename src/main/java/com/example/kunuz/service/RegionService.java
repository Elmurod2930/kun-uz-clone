package com.example.kunuz.service;

import com.example.kunuz.dto.region.RegionDTO;
import com.example.kunuz.dto.region.RegionRequestDTO;
import com.example.kunuz.entity.RegionEntity;
import com.example.kunuz.exps.AppBadRequestException;
import com.example.kunuz.exps.ProfileNotFoundException;
import com.example.kunuz.exps.RegionNotFoundException;
import com.example.kunuz.repository.RegionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class RegionService {
    private final RegionRepository regionRepository;

    public RegionRequestDTO create(RegionRequestDTO dto) {
        isValidRegion(dto);
        RegionEntity entity = new RegionEntity();
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        regionRepository.save(entity);
        return dto;
    }

    public void isValidRegion(RegionRequestDTO dto) {
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
            entity.setId(id);
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
        Iterable<RegionEntity> entityIterable = regionRepository.getAll();
        List<RegionDTO> dtoList = new LinkedList<>();
        for (RegionEntity entity : entityIterable) {
            dtoList.add(entityToDTO(entity));
        }
        return dtoList;
    }

    public RegionDTO entityToDTO(RegionEntity entity) {
        RegionDTO dto = new RegionDTO();
        dto.setNameUz(entity.getNameUz());
        dto.setNameEn(entity.getNameEn());
        dto.setNameRu(entity.getNameRu());
        return dto;
    }

    public List<RegionDTO> getByLang(String lang) {
        List<RegionDTO> dtoList = new LinkedList<>();
        switch (lang) {
            case "en" -> {
                for (RegionEntity entity : regionRepository.findByNameEng()) {
                    dtoList.add(entityToDTO(entity));
                }
            }
            case "ru" -> {
                for (RegionEntity entity : regionRepository.findByNameRu()) {
                    dtoList.add(entityToDTO(entity));
                }
            }
            case "uz" -> {
                for (RegionEntity entity : regionRepository.findByNameUz()) {
                    dtoList.add(entityToDTO(entity));
                }
            }
            default -> throw new AppBadRequestException("not found '" + lang + "'");
        }
        return dtoList;
    }

    public RegionEntity get(Integer regionId) {
        Optional<RegionEntity> optional = regionRepository.findById(regionId);
        if (optional.isEmpty()) {
            throw new ProfileNotFoundException("profile not found");
        }
        return optional.get();
    }

    public RegionEntity get(String regionName) {
        RegionEntity region = regionRepository.getByName(regionName);
        if (region == null) {
            throw new RegionNotFoundException("not fount region");
        }
        return region;
    }
}

package com.example.kunuz.service;

import com.example.kunuz.dto.category.CategoryDTO;
import com.example.kunuz.dto.category.CategoryRequestDTO;
import com.example.kunuz.entity.CategoryEntity;
import com.example.kunuz.exps.AppBadRequestException;
import com.example.kunuz.exps.CategoryNotFoundException;
import com.example.kunuz.exps.ProfileNotFoundException;
import com.example.kunuz.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryDTO> getByLang(String lang) {
        List<CategoryEntity> entityList;
        List<CategoryDTO> dtoList = new LinkedList<>();
        entityList = switch (lang) {
            case "en" -> categoryRepository.findByNameEn();
            case "ru" -> categoryRepository.findByNameRu();
            case "uz" -> categoryRepository.findByNameUz();
            default -> throw new AppBadRequestException("not found '" + lang + "'");
        };
        for (CategoryEntity entity : entityList) {
            dtoList.add(entityToDTO(entity));
        }
        return dtoList;
    }

    public CategoryRequestDTO create(CategoryRequestDTO dto) {
        isValidRegion(dto);
        CategoryEntity entity = new CategoryEntity();
        entity.setNameEn(dto.getNameEn());
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        categoryRepository.save(entity);
        return dto;
    }

    public void isValidRegion(CategoryRequestDTO dto) {
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

    public CategoryDTO updateById(Integer id, CategoryDTO dto) {
        Optional<CategoryEntity> optional = categoryRepository.findById(id);
        if (optional.isPresent()) {
            CategoryEntity entity = optional.get();
            if (dto.getNameUz() != null) {
                entity.setNameUz(dto.getNameUz());
            }
            if (dto.getNameRu() != null) {
                entity.setNameRu(dto.getNameRu());
            }
            if (dto.getNameEn() != null) {
                entity.setNameEn(dto.getNameEn());
            }
            categoryRepository.save(entity);
            return dto;
        }
        throw new CategoryNotFoundException("category not found");
    }

    public Boolean deleteById(Integer id) {
        Optional<CategoryEntity> optional = categoryRepository.findById(id);
        if (optional.isPresent()) {
            CategoryEntity entity = optional.get();
            entity.setVisible(false);
            categoryRepository.save(entity);
            return true;
        }
        throw new CategoryNotFoundException("not found category");
    }

    public List<CategoryDTO> getAll() {
        Iterable<CategoryEntity> entityIterable = categoryRepository.findAll();
        List<CategoryDTO> dtoList = new LinkedList<>();
        for (CategoryEntity entity : entityIterable) {
            dtoList.add(entityToDTO(entity));
        }
        return dtoList;
    }

    public CategoryDTO entityToDTO(CategoryEntity entity) {
        CategoryDTO dto = new CategoryDTO();
        dto.setNameEn(entity.getNameEn());
        dto.setNameUz(entity.getNameUz());
        dto.setNameRu(entity.getNameRu());
        return dto;
    }

    public CategoryEntity get(Integer categoryId) {
        Optional<CategoryEntity> optional = categoryRepository.findById(categoryId);
        if (optional.isEmpty()) {
            throw new ProfileNotFoundException("profile not found");
        }
        return optional.get();
    }
}

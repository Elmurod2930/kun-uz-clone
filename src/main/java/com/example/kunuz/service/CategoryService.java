package com.example.kunuz.service;

import com.example.kunuz.dto.CategoryDTO;
import com.example.kunuz.entity.CategoryEntity;
import com.example.kunuz.exps.AppBadRequestException;
import com.example.kunuz.exps.CategoryNotFoundException;
import com.example.kunuz.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryDTO> getByLang(String lang) {
        List<CategoryEntity> entityList = null;
        List<CategoryDTO> dtoList = new LinkedList<>();
        if (lang.equals("en")) {
            entityList = categoryRepository.findByNameEn();
        } else if (lang.equals("ru")) {
            entityList = categoryRepository.findByNameRu();
        } else if (lang.equals("uz")) {
            entityList = categoryRepository.findByNameUz();
        } else {
            throw new AppBadRequestException("not found '" + lang + "'");
        }
        for (CategoryEntity entity : entityList) {
            dtoList.add(entityToDTO(entity));
        }
        return dtoList;
    }

    public CategoryDTO create(CategoryDTO dto) {
        isValidRegion(dto);
        CategoryEntity entity = new CategoryEntity();
        entity.setNameEn(dto.getNameEn());
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        categoryRepository.save(entity);
        return dto;
    }

    public void isValidRegion(CategoryDTO dto) {
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
            dto.setId(entity.getId());
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
        dto.setId(entity.getId());
        dto.setNameEn(entity.getNameEn());
        dto.setNameUz(entity.getNameUz());
        dto.setNameRu(entity.getNameRu());
        return dto;
    }
}

package com.example.kunuz.service;

import com.example.kunuz.dto.ArticleTypeDTO;
import com.example.kunuz.entity.ArticleTypeEntity;
import com.example.kunuz.exps.AppBadRequestException;
import com.example.kunuz.exps.ArticleTypeNotFoundException;
import com.example.kunuz.repository.ArticleTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleTypeService {
    @Autowired
    private ArticleTypeRepository articleTypeRepository;

    public ArticleTypeDTO updateById(Integer id, ArticleTypeDTO dto) {
        Optional<ArticleTypeEntity> optional = articleTypeRepository.findById(id);
        if (optional.isPresent()) {
            ArticleTypeEntity entity = optional.get();
            if (dto.getNameUz() != null) {
                entity.setNameUz(dto.getNameUz());
            }
            if (dto.getNameEn() != null) {
                entity.setNameEn(dto.getNameEn());
            }
            if (dto.getNameRu() != null) {
                entity.setNameRu(dto.getNameRu());
            }
            articleTypeRepository.save(entity);
            dto.setId(id);
            return dto;
        }
        throw new AppBadRequestException("not found article type");
    }

    public ArticleTypeDTO create(ArticleTypeDTO dto, Integer adminId) {
        isValidProfile(dto);
        ArticleTypeEntity entity = new ArticleTypeEntity();
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        entity.setPrtId(adminId);
        articleTypeRepository.save(entity);
        return dto;
    }

    public void isValidProfile(ArticleTypeDTO dto) {
        // throw ...
        if (dto.getNameUz() == null) {
            throw new AppBadRequestException("invalid name uz");
        }
        if (dto.getNameEn() == null) {
            throw new AppBadRequestException("invalid name en");
        }
        if (dto.getNameRu() == null) {
            throw new AppBadRequestException("invalid name ru");
        }
    }

    public Boolean deleteById(Integer id) {
        Optional<ArticleTypeEntity> optional = articleTypeRepository.findById(id);
        if (optional.isPresent()) {
            ArticleTypeEntity entity = optional.get();
            entity.setVisible(false);
            articleTypeRepository.save(entity);
            return true;
        }
        throw new ArticleTypeNotFoundException("article type not found");
    }

    public List<ArticleTypeDTO> getAll() {
        Iterable<ArticleTypeEntity> entityList = articleTypeRepository.findAll();
        List<ArticleTypeDTO> dtoList = new LinkedList<>();
        for (ArticleTypeEntity entity : entityList) {
            dtoList.add(entityToDTO(entity));
        }
        return dtoList;
    }

    public ArticleTypeDTO entityToDTO(ArticleTypeEntity entity) {
        ArticleTypeDTO dto = new ArticleTypeDTO();
        dto.setId(entity.getId());
        dto.setNameEn(entity.getNameEn());
        dto.setNameRu(entity.getNameRu());
        dto.setNameUz(entity.getNameUz());
        return dto;
    }

    public List<ArticleTypeDTO> getByLang(String lang) {
        List<ArticleTypeEntity> entityList = null;
        if (lang.equals("en")) {
            entityList = articleTypeRepository.findByNameEng();
        } else if (lang.equals("ru")) {
            entityList = articleTypeRepository.findByNameRu();
        } else if (lang.equals("uz")) {
            entityList = articleTypeRepository.findByNameUz();
        } else {
            throw new AppBadRequestException("not found '" + lang + "'");
        }
        List<ArticleTypeDTO> dtoList = new LinkedList<>();
        for (ArticleTypeEntity entity : entityList) {
            dtoList.add(entityToDTO(entity));
        }
        return dtoList;
    }
}

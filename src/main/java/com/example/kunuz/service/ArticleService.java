package com.example.kunuz.service;

import com.example.kunuz.dto.ArticleDTO;
import com.example.kunuz.dto.jwt.JwtDTO;
import com.example.kunuz.entity.ArticleEntity;
import com.example.kunuz.entity.ArticleTypeEntity;
import com.example.kunuz.entity.CategoryEntity;
import com.example.kunuz.entity.RegionEntity;
import com.example.kunuz.enums.PublisherStatus;
import com.example.kunuz.exps.AppBadRequestException;
import com.example.kunuz.exps.ArticleNotFoundException;
import com.example.kunuz.exps.ArticleTypeNotFoundException;
import com.example.kunuz.repository.ArticleRepository;
import com.example.kunuz.repository.ArticleTypeRepository;
import com.example.kunuz.repository.CategoryRepository;
import com.example.kunuz.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private ArticleTypeRepository articleTypeRepository;

    public ArticleDTO create(JwtDTO jwtDTO, ArticleDTO dto) {
        isValidDTO(dto);
        ArticleEntity entity = new ArticleEntity();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setContent(dto.getContent());
        entity.setImageId(dto.getImage_id());
        entity.setRegion(regionRepository.findById(dto.getRegion_id()).get());
        entity.setCategory(categoryRepository.findById(dto.getCategory_id()).get());
        articleRepository.save(entity);
        return dto;
    }

    public void isValidDTO(ArticleDTO dto) {
        if (dto.getCategory_id() == null) {
            throw new AppBadRequestException("invalid category");
        }
        Optional<CategoryEntity> category = categoryRepository.findById(dto.getCategory_id());
        if (category.isEmpty()) {
            throw new AppBadRequestException("invalid category");
        }
        if (dto.getDescription() == null) {
            throw new AppBadRequestException("invalid description");
        }
        if (dto.getRegion_id() == null) {
            throw new AppBadRequestException("invalid region");
        }
        Optional<RegionEntity> region = regionRepository.findById(dto.getRegion_id());
        if (region.isEmpty()) {
            throw new AppBadRequestException("invalid region");
        }
        if (dto.getTitle() == null) {
            throw new AppBadRequestException("invalid title");
        }
        // todo image check
        if (dto.getImage_id() == null) {
            throw new AppBadRequestException("invalid image");
        }
        if (dto.getContent() == null) {
            throw new AppBadRequestException("invalid content");
        }
    }

    public ArticleDTO update(ArticleDTO dto, String articleId) {
        Optional<ArticleEntity> optional = articleRepository.findById(articleId);
        if (optional.isPresent()) {
            ArticleEntity entity = optional.get();
            if (dto.getContent() != null) {
                entity.setContent(dto.getContent());
            }
            if (dto.getDescription() != null) {
                entity.setDescription(entity.getDescription());
            }
            if (dto.getTitle() != null) {
                entity.setTitle(dto.getTitle());
            }
            // TODO: image check
            if (dto.getImage_id() != null) {
                entity.setImageId(dto.getImage_id());
            }
            if (dto.getCategory_id() != null) {
                Optional<CategoryEntity> category = categoryRepository.findById(dto.getCategory_id());
                if (category.isEmpty()) {
                    throw new AppBadRequestException("invalid category");
                }
                entity.setCategory(category.get());
            }
            if (dto.getRegion_id() != null) {
                Optional<RegionEntity> region = regionRepository.findById(dto.getRegion_id());
                if (region.isEmpty()) {
                    throw new AppBadRequestException("invalid region");
                }
                entity.setRegion(region.get());
            }
            articleRepository.save(entity);
            return dto;
        }
        throw new ArticleNotFoundException("not found article");
    }

    public Boolean delete(String id) {
        Optional<ArticleEntity> optional = articleRepository.findById(id);
        if (optional.isPresent()) {
            ArticleEntity entity = optional.get();
            entity.setVisible(false);
            articleRepository.save(entity);
            return true;
        }
        throw new ArticleNotFoundException("not found article");
    }

    public ArticleDTO changeStatus(String id) {
        Optional<ArticleEntity> optional = articleRepository.findById(id);
        if (optional.isPresent()) {
            ArticleEntity entity = optional.get();
            if (entity.getStatus().equals(PublisherStatus.PUBLISHED)) {
                entity.setStatus(PublisherStatus.NOT_PUBLISHED);
            } else {
                entity.setStatus(PublisherStatus.PUBLISHED);
            }
            articleRepository.save(entity);
            return entityToDTO(entity);
        }
        throw new ArticleNotFoundException("article not found");
    }

    public List<ArticleDTO> articleShortInfo(Integer typeId, Integer count) {
        Optional<ArticleTypeEntity> optional = articleTypeRepository.findById(typeId);
        if (optional.isEmpty()) {
            throw new ArticleTypeNotFoundException("not found type");
        }
        ArticleTypeEntity typeEntity = optional.get();
        List<ArticleEntity> entityList = articleRepository.findAllByType(typeEntity, count);
        List<ArticleDTO> dtoList = new LinkedList<>();
        for (ArticleEntity entity : entityList) {
            dtoList.add(entityToDTO(entity));
        }
        return dtoList;
    }

    public ArticleDTO entityToDTO(ArticleEntity entity) {
        ArticleDTO dto = new ArticleDTO();
        dto.setDescription(entity.getDescription());
        dto.setContent(entity.getContent());
        dto.setCategory_id(entity.getCategory().getId());
        dto.setTitle(entity.getTitle());
        dto.setRegion_id(entity.getRegion().getId());
        return dto;
    }

    public List<ArticleDTO> articleShortInfo(List<Integer> idList) {
        List<ArticleEntity> entityList = articleRepository.articleShortInfo(idList);
        List<ArticleDTO> dtoList = new LinkedList<>();
        for (ArticleEntity entity : entityList) {
            dtoList.add(entityToDTO(entity));
        }
        return dtoList;
    }
}

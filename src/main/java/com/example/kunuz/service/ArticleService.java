package com.example.kunuz.service;

import com.example.kunuz.dto.article.ArticleDTO;
import com.example.kunuz.dto.article.ArticleFullInfoDTO;
import com.example.kunuz.dto.article.ArticleResponseDTO;
import com.example.kunuz.dto.article.ArticleShortInfoDTO;
import com.example.kunuz.entity.*;
import com.example.kunuz.enums.PublisherStatus;
import com.example.kunuz.exps.AppBadRequestException;
import com.example.kunuz.exps.ArticleNotFoundException;
import com.example.kunuz.repository.ArticleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ArticleService {

    private final CategoryService categoryService;
    private final RegionService regionService;
    private final ArticleTypeService articleTypeService;
    private final ProfileService profileService;
    private final AttachService attachService;
    private final ArticleRepository articleRepository;

    public ArticleResponseDTO create(ArticleResponseDTO dto, Integer moderId) {
        // check
        isValidDTO(dto);
        ProfileEntity moderator = profileService.get(moderId);
        RegionEntity region = regionService.get(dto.getRegionId());
        CategoryEntity category = categoryService.get(dto.getCategoryId());

        ArticleEntity entity = new ArticleEntity();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setContent(dto.getContent());
        entity.setRegion(region);
        entity.setCategory(category);
        entity.setModerator(moderator);
        // type
        articleRepository.save(entity);
        return dto;
    }

    public void isValidDTO(ArticleResponseDTO dto) {
        if (dto.getDescription() == null) {
            throw new AppBadRequestException("invalid description");
        }
        if (dto.getTitle() == null) {
            throw new AppBadRequestException("invalid title");
        }
        // todo image check
        if (dto.getAttachId() == null) {
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
            if (dto.getAttachId() != null) {
                entity.setAttach(attachService.get(dto.getAttachId()));
            }
            if (dto.getCategoryId() != null) {
                entity.setCategory(categoryService.get(dto.getCategoryId()));
            }
            if (dto.getRegionId() != null) {
                entity.setRegion(regionService.get(dto.getRegionId()));
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

    public List<ArticleDTO> articleShortInfo(Integer typeId) {
        ArticleTypeEntity typeEntity = articleTypeService.get(typeId);
        List<ArticleEntity> entityList = articleRepository.findAllByType(typeEntity);
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
        dto.setCategoryId(entity.getCategory().getId());
        dto.setTitle(entity.getTitle());
        dto.setRegionId(entity.getRegion().getId());
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

    public ArticleEntity get(String id) {
        Optional<ArticleEntity> optional = articleRepository.findById(id);
        if (optional.isEmpty()) {
            throw new ArticleNotFoundException("article not found");
        }
        return optional.get();
    }

    public ArticleFullInfoDTO articleFullInfo(String id, String lang) {
        ArticleEntity entity = get(id);
        ArticleFullInfoDTO dto = new ArticleFullInfoDTO();
        dto.setId(entity.getId());
        dto.setRegion(entity.getRegion());
        dto.setPublishedDate(entity.getPublishedDate());
        dto.setSharedCount(entity.getSharedCount());
        dto.setViewCount(entity.getViewCount());
        dto.setDescription(entity.getDescription());
        dto.setContent(entity.getContent());
        dto.setTitle(entity.getTitle());
        return dto;
    }

    public List<ArticleShortInfoDTO> articleShortInfo() {
        List<ArticleEntity> entityList = articleRepository.mostReadArticles();
        List<ArticleShortInfoDTO> dtoList = new LinkedList<>();
        for (ArticleEntity entity : entityList) {
            dtoList.add(toShortInfo(entity));
        }
        return dtoList;
    }

    public ArticleShortInfoDTO toShortInfo(ArticleEntity entity) {
        ArticleShortInfoDTO dto = new ArticleShortInfoDTO();
        dto.setAttach(entity.getAttach());
        dto.setId(entity.getId());
        dto.setPublishedDate(entity.getPublishedDate());
        dto.setDescription(entity.getDescription());
        dto.setTitle(entity.getTitle());
        return dto;
    }

    public List<ArticleShortInfoDTO> articleShortInfo(String type, String regionName) {
        ArticleTypeEntity typeEntity = articleTypeService.get(type);
        RegionEntity region = regionService.get(regionName);
        List<ArticleEntity> entityList = articleRepository.findByTypeAndRegion(typeEntity, region);
        List<ArticleShortInfoDTO> dtoList = new LinkedList<>();
        for (ArticleEntity entity : entityList) {
            dtoList.add(toShortInfo(entity));
        }
        return dtoList;
    }
}

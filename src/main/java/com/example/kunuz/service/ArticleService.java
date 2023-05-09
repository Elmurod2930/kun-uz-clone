package com.example.kunuz.service;

import com.example.kunuz.dto.article.ArticleDTO;
import com.example.kunuz.dto.attach.AttachDTO;
import com.example.kunuz.dto.article.ArticleFullInfoDTO;
import com.example.kunuz.dto.article.ArticleResponseDTO;
import com.example.kunuz.dto.article.ArticleShortInfoDTO;
import com.example.kunuz.entity.*;
import com.example.kunuz.enums.PublisherStatus;
import com.example.kunuz.exps.AppBadRequestException;
import com.example.kunuz.exps.ArticleNotFoundException;
import com.example.kunuz.mapper.ArticleShortInfoMapper;
import com.example.kunuz.repository.ArticleRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.swing.event.ListDataEvent;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
//        RegionEntity region = regionService.get(dto.getRegionId());
//        CategoryEntity category = categoryService.get(dto.getCategoryId());

        ArticleEntity entity = new ArticleEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setContent(dto.getContent());
//        entity.setRegion(region);
//        entity.setCategory(category);
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


    public List<ArticleDTO> get5TypeArticle(Integer typeId) {
        ArticleTypeEntity typeEntity = articleTypeService.get(typeId);
        List<ArticleEntity> entityList = articleRepository.findAllByType5(typeEntity);
        List<ArticleDTO> dtoList = new LinkedList<>();
        for (ArticleEntity entity : entityList) {
            dtoList.add(entityToDTO(entity));
        }
        return dtoList;
    }

    public List<ArticleDTO> get3TypeArticle(Integer typeId) {
        ArticleTypeEntity typeEntity = articleTypeService.get(typeId);
        List<ArticleEntity> entityList = articleRepository.findAllByType3(typeEntity);
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
        dto.setRegion(regionService.entityToDTO(entity.getRegion()));
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
        dto.setAttach(attachService.getAttachLink(entity.getAttachId()));
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

    public List<ArticleShortInfoDTO> getRegionArticle(Integer id, int size, int page) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<ArticleEntity> entityPage = articleRepository.findByRegionId(id, pageable);
        long totalCount = entityPage.getTotalElements();
        List<ArticleEntity> entityList = entityPage.getContent();
        List<ArticleShortInfoDTO> dtoList = new LinkedList<>();
        for (ArticleEntity entity : entityList) {
            dtoList.add(toShortInfo(entity));
        }
        return dtoList;
    }

    public List<ArticleShortInfoDTO> get5CategoryArticle(Integer id) {
        CategoryEntity category = categoryService.get(id);
        List<ArticleEntity> entityList = articleRepository.get5CategoryArticle(category);
        List<ArticleShortInfoDTO> dtoList = new LinkedList<>();
        for (ArticleEntity entity : entityList) {
            dtoList.add(toShortInfo(entity));
        }
        return dtoList;
    }

    public List<ArticleShortInfoDTO> getCategoryArticle(Integer id, int size, int page) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<ArticleEntity> entityPage = articleRepository.findByCategoryId(id, pageable);
        long totalCount = entityPage.getTotalElements();
        List<ArticleEntity> entityList = entityPage.getContent();
        List<ArticleShortInfoDTO> dtoList = new LinkedList<>();
        for (ArticleEntity entity : entityList) {
            dtoList.add(toShortInfo(entity));
        }
        return dtoList;
    }

    public List<ArticleShortInfoDTO> get4ArticleByTypes(Integer typeId, String articleId) {
        ArticleTypeEntity type = articleTypeService.get(typeId);
        List<ArticleEntity> entityList = articleRepository.findByTypeIdAndIdNot(type, articleId);
        List<ArticleShortInfoDTO> dtoList = new LinkedList<>();
        for (ArticleEntity entity : entityList) {
            dtoList.add(toShortInfo(entity));
        }
        return dtoList;
    }

    // ===========================================================================================================
    public ArticleShortInfoDTO toArticleShortInfo(ArticleEntity entity) {
        ArticleShortInfoDTO dto = new ArticleShortInfoDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setPublishedDate(entity.getPublishedDate());
        dto.setAttach(attachService.getAttachLink(entity.getAttachId()));
        return dto;
    }

    public List<ArticleShortInfoDTO> getLast5ByTypeId(Integer typeId) {
        List<ArticleEntity> entityList = articleRepository.findTop5ByTypeIdAndStatusAndVisibleOrderByCreatedDateDesc(typeId,
                PublisherStatus.PUBLISHED, true);
        List<ArticleShortInfoDTO> dtoList = new LinkedList<>();
        entityList.forEach(entity -> {
            dtoList.add(toArticleShortInfo(entity));
        });
        return dtoList;
    }


    public Boolean changeStatus(PublisherStatus status, String id, Integer prtId) {
        ArticleEntity entity = get(id);
        if (status.equals(PublisherStatus.PUBLISHED)) {
            entity.setPublishedDate(LocalDateTime.now());
            entity.setPublisherId(prtId);
        }
        entity.setStatus(status);
        articleRepository.save(entity);
        // articleRepository.changeStatus(status, id);
        return true;
    }

    public ArticleShortInfoDTO toArticleShortInfo(ArticleShortInfoMapper entity) {
        ArticleShortInfoDTO dto = new ArticleShortInfoDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setPublishedDate(entity.getPublished_date());
        dto.setAttach(attachService.getAttachLink(entity.getAttachId()));
        return dto;
    }

    public ArticleDTO articleViewCount(String id) {
        ArticleEntity entity = get(id);
        entity.setViewCount(entity.getViewCount() + 1);
        articleRepository.save(entity);
        return entityToDTO(entity);
    }

    public ArticleDTO articleShareCount(String id) {
        ArticleEntity entity = get(id);
        entity.setSharedCount(entity.getSharedCount() + 1);
        articleRepository.save(entity);
        return entityToDTO(entity);
    }
}

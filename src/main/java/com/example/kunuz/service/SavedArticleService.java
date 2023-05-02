package com.example.kunuz.service;

import com.example.kunuz.dto.article.SavedArticleDTO;
import com.example.kunuz.dto.article.SavedArticleRequestDTO;
import com.example.kunuz.dto.article.SavedArticleResponseDTO;
import com.example.kunuz.entity.ArticleEntity;
import com.example.kunuz.entity.SavedArticleEntity;
import com.example.kunuz.exps.ItemNotFoundException;
import com.example.kunuz.repository.SavedArticleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SavedArticleService {
    private final SavedArticleRepository savedArticleRepository;
    private final ArticleService articleService;

    public SavedArticleRequestDTO create(SavedArticleRequestDTO dto, String articleId) {
        ArticleEntity article = articleService.get(articleId);
        SavedArticleEntity entity = new SavedArticleEntity();
        entity.setArticleId(articleId);
        entity.setProfileId(dto.getProfileId());
        entity.setArticle(article);
        savedArticleRepository.save(entity);
        dto.setArticleId(articleId);
        return dto;
    }

    public Boolean delete(Integer id, Integer profileId) {
        get(id);
        return savedArticleRepository.deleteById(id, profileId);
    }

    public SavedArticleEntity get(Integer id) {
        Optional<SavedArticleEntity> optional = savedArticleRepository.findById(id);
        if (optional.isEmpty()) {
            throw new ItemNotFoundException("saved article not fount");
        }
        return optional.get();
    }

    public List<SavedArticleResponseDTO> getList(Integer profileId) {
        List<SavedArticleResponseDTO> list = savedArticleRepository.getList(profileId);
        return list;
    }
}
